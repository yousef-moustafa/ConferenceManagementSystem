package controller;

import model.dto.SessionDTO;
import model.dto.SpeakerDTO;
import model.service.AuthService;
import model.service.SessionService;
import model.service.SpeakerService;
import view.Login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

public class SpeakerController {
    private final SpeakerService speakerService;
    private final SessionService sessionService;
    private final AuthService authService;

    public SpeakerController(SpeakerService speakerService, SessionService sessionService, AuthService authService) {
        this.speakerService = speakerService;
        this.sessionService = sessionService;
        this.authService = authService;
    }

    public void loadSpeakerSessions(DefaultTableModel sessionTableModel, String speakerID) {
        sessionTableModel.setRowCount(0); // Clear existing rows

        List<SessionDTO> sessions = speakerService.getSpeakerSessions(speakerID);
        for (SessionDTO session : sessions) {
            sessionTableModel.addRow(new Object[]{
                    session.getSessionID(),
                    session.getSessionName(),
            });
        }
    }

    public void loadSpeakerBio(String speakerID, JTextArea speakerBio) {
        SpeakerDTO speaker = speakerService.getSpeakerProfile(speakerID);
        if (speaker != null) {
            speakerBio.setText(speaker.getBio());
        } else {
            JOptionPane.showMessageDialog(null, "Failed to load bio for speaker.");
        }
    }

    public void saveSpeakerBio(String speakerID, JTextArea speakerBio) {
        String updatedBio = speakerBio.getText().trim();
        if (updatedBio.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Bio cannot be empty.");
            return;
        }

        try {
            speakerService.updateSpeakerBio(speakerID, updatedBio);
            JOptionPane.showMessageDialog(null, "Bio updated successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Failed to update bio: " + ex.getMessage());
        }
    }

    public void viewSessionDetails(DefaultTableModel sessionTableModel, JTable speakerSessionsTable) {
        int selectedRow = speakerSessionsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a session to view details.");
            return;
        }

        String sessionID = (String) sessionTableModel.getValueAt(selectedRow, 0);
        SessionDTO session = sessionService.getSession(sessionID);

        if (session != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(session.getDate());

            JOptionPane.showMessageDialog(null, "Session Details:\n" +
                    "Title: " + session.getSessionName() + "\n" +
                    "Date: " + formattedDate + "\n" +
                    "Time: " + session.getTime() + "\n" +
                    "Room: " + session.getRoom() + "\n" +
                    "Capacity: " + session.getCapacity() + "\n" +
                    "Status: " + session.getStatus());
        } else {
            JOptionPane.showMessageDialog(null, "Session details could not be retrieved.");
        }
    }

    public void handleLogout(JFrame currentUI) {
        int confirm = JOptionPane.showConfirmDialog(
                currentUI,
                "Are you sure you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            authService.logout();
            Login loginScreen = new Login();
            loginScreen.setVisible(true);
            currentUI.dispose();
        }
    }
}
