package view;

import model.dto.SessionDTO;
import model.dto.SpeakerDTO;
import model.service.SessionService;
import model.service.SpeakerService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

public class SpeakerPortalUI extends JFrame {
    private JPanel SpeakerPortalUI;
    private JTabbedPane tabbedPane1;
    private JTable speakerSessionsTable;
    private JButton viewSessionDetailsButton;
    private JTextArea speakerBio;
    private JButton saveBioButton;
    private JLabel statusBar;

    public SpeakerPortalUI(String speakerID, String speakerName) {
        setContentPane(SpeakerPortalUI);
        setTitle("Speaker Portal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultTableModel speakerSessionTableModel = new DefaultTableModel(
                new String[]{"ID", "Session Title"}, 0 // Only ID and Title columns
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing
            }
        };
        speakerSessionsTable.setModel(speakerSessionTableModel);

        // Hide the ID column
        speakerSessionsTable.getColumnModel().getColumn(0).setMinWidth(0);
        speakerSessionsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        speakerSessionsTable.getColumnModel().getColumn(0).setPreferredWidth(0);

        // Center align the Title column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        speakerSessionsTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        SpeakerService speakerService = new SpeakerService();

        // Load sessions for the logged-in speaker
        loadSpeakerSessions(speakerSessionTableModel, speakerService, speakerID);
        loadSpeakerBio(speakerID, speakerService);

        // Update the status bar
        statusBar.setText("Logged in as: " + speakerName + " | id: " + speakerID);

        viewSessionDetailsButton.addActionListener(e -> viewSessionDetails((DefaultTableModel) speakerSessionsTable.getModel()));
        saveBioButton.addActionListener(e -> saveSpeakerBio(speakerID, new SpeakerService()));
    }

    private void loadSpeakerSessions(DefaultTableModel sessionTableModel, SpeakerService speakerService, String speakerID) {
        sessionTableModel.setRowCount(0); // Clear existing rows

        // Fetch the sessions assigned to the speaker
        List<SessionDTO> sessions = speakerService.getSpeakerSessions(speakerID);

        // Populate the table with session data
        for (SessionDTO session : sessions) {
            sessionTableModel.addRow(new Object[]{
                    session.getSessionID(),
                    session.getSessionName(),
            });
        }
    }

    private void viewSessionDetails(DefaultTableModel sessionTableModel) {
        int selectedRow = speakerSessionsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a session to view details.");
            return;
        }

        // Retrieve the session ID from the hidden column
        String sessionID = (String) sessionTableModel.getValueAt(selectedRow, 0);

        // Fetch session details
        SessionDTO session = new SessionService().getSession(sessionID);

        if (session != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(session.getDate());

            // Display session details in a dialog
            JOptionPane.showMessageDialog(this, "Session Details:\n" +
                    "Title: " + session.getSessionName() + "\n" +
                    "Date: " + formattedDate + "\n" + // Use formatted date
                    "Time: " + session.getTime() + "\n" +
                    "Room: " + session.getRoom() + "\n" +
                    "Capacity: " + session.getCapacity() + "\n" +
                    "Status: " + session.getStatus());
        } else {
            JOptionPane.showMessageDialog(this, "Session details could not be retrieved.");
        }
    }

    private void loadSpeakerBio(String speakerID, SpeakerService speakerService) {
        SpeakerDTO speaker = speakerService.getSpeakerProfile(speakerID);
        if (speaker != null) {
            speakerBio.setText(speaker.getBio()); // Set the current bio in the JTextArea
        } else {
            JOptionPane.showMessageDialog(this, "Failed to load bio for speaker.");
        }
    }

    private void saveSpeakerBio(String speakerID, SpeakerService speakerService) {
        String updatedBio = speakerBio.getText().trim(); // Fetch the updated bio from the JTextArea

        if (updatedBio.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bio cannot be empty.");
            return;
        }

        try {
            // Update the bio using the SpeakerService
            speakerService.updateSpeakerBio(speakerID, updatedBio);

            JOptionPane.showMessageDialog(this, "Bio updated successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to update bio: " + ex.getMessage());
        }
    }

}