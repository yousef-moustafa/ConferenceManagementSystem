package controller;

import model.domain.PersonalizedSchedule;
import model.dto.CertificateDTO;
import model.dto.SessionDTO;
import model.dto.SpeakerDTO;
import model.service.*;
import view.Login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AttendeeController {
    private final SessionService sessionService;
    private final SpeakerService speakerService;
    private final AttendeeService attendeeService;
    private final CertificateService certificateService;
    private final AuthService authService;

    public AttendeeController(SessionService sessionService, SpeakerService speakerService,
                              AttendeeService attendeeService, CertificateService certificateService,
                              AuthService authService) {
        this.sessionService = sessionService;
        this.speakerService = speakerService;
        this.attendeeService = attendeeService;
        this.certificateService = certificateService;
        this.authService = authService;
    }

    public void loadConferenceSchedule(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);

        List<SessionDTO> sessions = sessionService.getAllSessions();

        for (SessionDTO session : sessions) {
            String speakerName = "";
            String speakerBio = "";
            if (session.getSpeakerID() != null) {
                SpeakerDTO speaker = speakerService.getSpeakerProfile(session.getSpeakerID());
                if (speaker != null) {
                    speakerName = speaker.getName();
                    speakerBio = speaker.getBio();
                }
            }
            tableModel.addRow(new Object[]{
                    session.getSessionID(),
                    session.getSessionName(),
                    speakerName,
                    speakerBio,
                    session.getDate(),
                    session.getTime(),
                    session.getRoom(),
            });
        }
    }

    public void loadPersonalizedSchedule(DefaultTableModel tableModel, String attendeeID) {
        tableModel.setRowCount(0);

        PersonalizedSchedule schedule = attendeeService.getAttendeeSchedule(attendeeID);

        if (schedule == null || schedule.getSessionsIDs().isEmpty()) {
            return;
        }

        for (String sessionID : schedule.getSessionsIDs()) {
            SessionDTO session = sessionService.getSession(sessionID);
            if (session != null) {
                String speakerName = "";
                String speakerBio = "";
                if (session.getSpeakerID() != null) {
                    SpeakerDTO speaker = speakerService.getSpeakerProfile(session.getSpeakerID());
                    if (speaker != null) {
                        speakerName = speaker.getName();
                        speakerBio = speaker.getBio();
                    }
                }
                tableModel.addRow(new Object[]{
                        session.getSessionID(),
                        session.getSessionName(),
                        speakerName,
                        speakerBio,
                        session.getDate(),
                        session.getTime(),
                        session.getRoom()
                });
            }
        }
    }

    public void registerForSession(DefaultTableModel tableModel, JTable table, String attendeeID) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a session to register.");
            return;
        }

        String sessionID = (String) tableModel.getValueAt(selectedRow, 0);

        boolean success = attendeeService.registerAttendeeForSession(attendeeID, sessionID);

        if (success) {
            JOptionPane.showMessageDialog(null, "You have successfully registered for the session!");
        } else {
            JOptionPane.showMessageDialog(null, "Registration failed. The session might already be in your schedule or conflicts with another session.");
        }
    }

    public void unregisterFromSession(DefaultTableModel tableModel, JTable table, String attendeeID) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a session to unregister.");
            return;
        }

        String sessionID = (String) tableModel.getValueAt(selectedRow, 0);

        boolean success = attendeeService.unregisterAttendeeFromSession(attendeeID, sessionID);

        if (success) {
            JOptionPane.showMessageDialog(null, "You have successfully unregistered from the session.");
        } else {
            JOptionPane.showMessageDialog(null, "Unregistration failed. The session might not be in your schedule.");
        }
    }

    public void submitFeedback(String attendeeID, int rating, String comment) {
        try {
            String ratingFeedbackID = attendeeService.submitRating(attendeeID, rating);

            if (!comment.isEmpty()) {
                String commentFeedbackID = attendeeService.submitComment(attendeeID, comment);
                JOptionPane.showMessageDialog(null, "Feedback submitted successfully!\n"
                        + "Rating ID: " + ratingFeedbackID + "\nComment ID: " + commentFeedbackID);
            } else {
                JOptionPane.showMessageDialog(null, "Rating submitted successfully!\nRating ID: " + ratingFeedbackID);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Submission Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void initializeCertificateTab(JLabel noCertificateLabel, JTextArea certificateDetails, String attendeeID) {
        try {
            CertificateDTO certificate = certificateService.getCertificateForAttendee(attendeeID);

            noCertificateLabel.setVisible(false);
            certificateDetails.setVisible(true);
            certificateDetails.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            certificateDetails.setText(certificate.getDisplayString());
        } catch (IllegalArgumentException e) {
            noCertificateLabel.setVisible(true);
            certificateDetails.setVisible(false);

            noCertificateLabel.setText("<html>Manager has not sent you a certificate.<br>Make sure you have attended all sessions before the conference ends.</html>");
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
