package view;

import controller.ManagerController;
import model.domain.enums.SessionStatus;
import model.dto.SessionDTO;
import model.dto.SpeakerDTO;
import main.ApplicationContext;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerPortalUI extends JFrame {
    private JPanel ManagerPortalUI;
    private JTabbedPane tabbedPane1;
    private JTable speakerTable;
    private JButton addSpeakerButton;
    private JButton editSpeakerButton;
    private JButton deleteSpeakerButton;
    private JTable sessionTable;
    private JButton addSessionButton;
    private JButton editSessionButton;
    private JButton deleteSessionButton;
    private JTable attendeeTable;
    private JTextField searchField;
    private JButton searchButton;
    private JButton markAttendanceButton;
    private JTable feedbackTable;
    private JButton exportFeedbackReportButton;
    private JComboBox sessionComboBox;
    private JTable sessionAttendeesTable;
    private JLabel averageFeedbackLabel;
    private JButton issueCertificatesButton;
    private JButton logoutButton;

    private final ManagerController controller;

    public ManagerPortalUI() {
        this.controller = ApplicationContext.getInstance().getManagerController();

        setContentPane(ManagerPortalUI);
        setTitle("Manager Portal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultTableModel speakerTableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Email", "Bio"}, 0
        );
        speakerTable.setModel(speakerTableModel);
        hideIDColumn(speakerTable);

        DefaultTableModel sessionTableModel = new DefaultTableModel(
                new String[]{"ID", "Title", "Speaker", "Date", "Time", "Room", "Capacity", "Status"}, 0
        );
        sessionTable.setModel(sessionTableModel);
        hideIDColumn(sessionTable);

        DefaultTableModel attendeeTableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Email", "Sessions Attended"}, 0
        );
        attendeeTable.setModel(attendeeTableModel);
        hideIDColumn(attendeeTable);

        DefaultTableModel sessionAttendeesTableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Email", "Attended"}, 0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 3 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        sessionAttendeesTable.setModel(sessionAttendeesTableModel);

        DefaultTableModel feedbackTableModel = new DefaultTableModel(
                new String[]{"Feedback ID", "Attendee ID", "Type", "Details"}, 0
        );
        feedbackTable.setModel(feedbackTableModel);

        // Initial load
        controller.loadSpeakers(speakerTableModel);
        controller.loadSessions(sessionTableModel);
        controller.loadAttendees(attendeeTableModel);

        tabbedPane1.addChangeListener(e -> {
            if (tabbedPane1.getSelectedIndex() == 1) {
                controller.loadSessionsIntoComboBox(sessionComboBox);
            }
            if (tabbedPane1.getSelectedIndex() == 2) {
                controller.loadFeedbackTable(feedbackTableModel);
                var report = controller.getConferenceFeedbackAnalysis();
                String avgRating = String.format("%.2f", report.getAverageRating());
                averageFeedbackLabel.setText("Total Responses: " + report.getTotalResponses() +
                        " | Average Feedback Rating: " + avgRating);
            }
        });

        addSpeakerButton.addActionListener(e -> handleAddSpeaker(speakerTableModel));
        editSpeakerButton.addActionListener(e -> handleEditSpeaker(speakerTableModel));
        deleteSpeakerButton.addActionListener(e -> handleDeleteSpeaker(speakerTableModel));

        addSessionButton.addActionListener(e -> handleAddSession(sessionTableModel));
        editSessionButton.addActionListener(e -> handleEditSession(sessionTableModel));
        deleteSessionButton.addActionListener(e -> handleDeleteSession(sessionTableModel));

        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (query.isEmpty()) controller.loadAttendees(attendeeTableModel);
            else controller.searchAttendees(attendeeTableModel, query);
        });

        sessionComboBox.addActionListener(e -> {
            String selectedSession = (String) sessionComboBox.getSelectedItem();
            if (selectedSession != null) {
                String sessionID = selectedSession.split(":")[0];
                controller.loadAttendeesForSession(sessionAttendeesTableModel, sessionID);
            }
        });

        markAttendanceButton.addActionListener(e -> handleMarkAttendance(sessionAttendeesTableModel, attendeeTableModel));

        issueCertificatesButton.addActionListener(e -> handleIssueCertificates());

        exportFeedbackReportButton.addActionListener(e -> handleExportFeedbackReport());

        logoutButton.addActionListener(e -> handleLogout());
    }

    private void handleAddSpeaker(DefaultTableModel speakerTableModel) {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextArea bioArea = new JTextArea(5, 20);
        JScrollPane bioScrollPane = new JScrollPane(bioArea);

        Object[] fields = {"Name:", nameField, "Email:", emailField, "Password:", passwordField, "Bio:", bioScrollPane};
        int result = JOptionPane.showConfirmDialog(null, fields, "Add Speaker", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                SpeakerDTO newSpeaker = new SpeakerDTO();
                newSpeaker.setName(nameField.getText().trim());
                newSpeaker.setEmail(emailField.getText().trim());
                newSpeaker.setBio(bioArea.getText().trim());
                newSpeaker.setAssociatedSessionIDs(new ArrayList<>());

                String speakerID = controller.addSpeaker(newSpeaker, new String(passwordField.getPassword()).trim(), speakerTableModel);
                JOptionPane.showMessageDialog(null, "Speaker added successfully! ID: " + speakerID);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to add speaker: " + ex.getMessage());
            }
        }
    }

    private void handleEditSpeaker(DefaultTableModel speakerTableModel) {
        int selectedRow = speakerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a speaker to edit.");
            return;
        }

        String speakerID = speakerTableModel.getValueAt(selectedRow, 0).toString();
        SpeakerDTO speaker = controller.getSpeakerProfile(speakerID);
        if (speaker == null) {
            JOptionPane.showMessageDialog(null, "Speaker not found.");
            return;
        }

        JTextField nameField = new JTextField(speaker.getName());
        JTextArea bioArea = new JTextArea(speaker.getBio(), 5, 20);
        JScrollPane bioScrollPane = new JScrollPane(bioArea);

        Object[] fields = {"Name:", nameField, "Bio:", bioScrollPane};
        int result = JOptionPane.showConfirmDialog(null, fields, "Edit Speaker", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                controller.updateSpeakerProfile(speaker.getSpeakerID(), nameField.getText().trim(), bioArea.getText().trim(), speakerTableModel);
                JOptionPane.showMessageDialog(null, "Speaker updated successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to update speaker: " + ex.getMessage());
            }
        }
    }

    private void handleDeleteSpeaker(DefaultTableModel speakerTableModel) {
        int selectedRow = speakerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a speaker to delete.");
            return;
        }

        String speakerID = speakerTableModel.getValueAt(selectedRow, 0).toString();
        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this speaker?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            try {
                controller.deleteSpeaker(speakerID, speakerTableModel);
                JOptionPane.showMessageDialog(null, "Speaker deleted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to delete speaker: " + ex.getMessage());
            }
        }
    }

    private void handleAddSession(DefaultTableModel sessionTableModel) {
        JTextField sessionNameField = new JTextField();
        JTextField dateField = new JTextField("YYYY-MM-DD");
        JTextField timeField = new JTextField("HH:MM");
        JTextField roomField = new JTextField();
        JTextField capacityField = new JTextField();

        List<SpeakerDTO> speakers = controller.getAllSpeakers();
        JComboBox<String> speakerComboBox = new JComboBox<>(speakers.stream().map(SpeakerDTO::getName).toArray(String[]::new));

        Object[] fields = {
                "Session Name:", sessionNameField,
                "Date (YYYY-MM-DD):", dateField,
                "Time (HH:MM):", timeField,
                "Room:", roomField,
                "Capacity:", capacityField,
                "Speaker:", speakerComboBox
        };

        int result = JOptionPane.showConfirmDialog(null, fields, "Add Session", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = sessionNameField.getText().trim();
                Date date = Date.valueOf(dateField.getText().trim());
                LocalTime time = LocalTime.parse(timeField.getText().trim());
                String room = roomField.getText().trim();
                int capacity = Integer.parseInt(capacityField.getText().trim());
                String speakerName = (String) speakerComboBox.getSelectedItem();

                // Find speaker ID
                String speakerID = null;
                for (SpeakerDTO s : speakers) {
                    if (s.getName().equals(speakerName)) {
                        speakerID = s.getSpeakerID();
                        break;
                    }
                }
                if (speakerID == null) throw new Exception("Speaker not found.");

                SessionDTO newSession = new SessionDTO();
                newSession.setSessionName(name);
                newSession.setSpeakerID(speakerID);
                newSession.setDate(date);
                newSession.setTime(time);
                newSession.setRoom(room);
                newSession.setCapacity(capacity);
                newSession.setStatus(SessionStatus.SCHEDULED);

                controller.addSession(newSession, speakerName, sessionTableModel);
                JOptionPane.showMessageDialog(null, "Session added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to add session: " + ex.getMessage());
            }
        }
    }

    private void handleEditSession(DefaultTableModel sessionTableModel) {
        int selectedRow = sessionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a session to edit.");
            return;
        }

        String sessionID = sessionTableModel.getValueAt(selectedRow, 0).toString();
        SessionDTO session = controller.getSession(sessionID);
        if (session == null) {
            JOptionPane.showMessageDialog(null, "Session not found.");
            return;
        }

        JTextField sessionNameField = new JTextField(session.getSessionName());
        JTextField dateField = new JTextField(session.getDate().toString());
        JTextField timeField = new JTextField(session.getTime().toString());
        JTextField roomField = new JTextField(session.getRoom());
        JTextField capacityField = new JTextField(String.valueOf(session.getCapacity()));

        List<SpeakerDTO> speakers = controller.getAllSpeakers();
        JComboBox<String> speakerComboBox = new JComboBox<>(speakers.stream().map(SpeakerDTO::getName).toArray(String[]::new));

        // Set current speaker in combo box
        for (SpeakerDTO sp : speakers) {
            if (sp.getSpeakerID().equals(session.getSpeakerID())) {
                speakerComboBox.setSelectedItem(sp.getName());
                break;
            }
        }

        Object[] fields = {
                "Session Name:", sessionNameField,
                "Date (YYYY-MM-DD):", dateField,
                "Time (HH:MM):", timeField,
                "Room:", roomField,
                "Capacity:", capacityField,
                "Speaker:", speakerComboBox
        };

        int result = JOptionPane.showConfirmDialog(null, fields, "Edit Session", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                session.setSessionName(sessionNameField.getText().trim());
                session.setDate(Date.valueOf(dateField.getText().trim()));
                session.setTime(LocalTime.parse(timeField.getText().trim()));
                session.setRoom(roomField.getText().trim());
                session.setCapacity(Integer.parseInt(capacityField.getText().trim()));

                String updatedSpeakerName = (String) speakerComboBox.getSelectedItem();
                String updatedSpeakerID = null;
                for (SpeakerDTO s : speakers) {
                    if (s.getName().equals(updatedSpeakerName)) {
                        updatedSpeakerID = s.getSpeakerID();
                        break;
                    }
                }
                if (updatedSpeakerID == null) throw new Exception("Speaker not found.");
                session.setSpeakerID(updatedSpeakerID);

                controller.updateSession(session, sessionTableModel);
                JOptionPane.showMessageDialog(null, "Session updated successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to update session: " + ex.getMessage());
            }
        }
    }

    private void handleDeleteSession(DefaultTableModel sessionTableModel) {
        int selectedRow = sessionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a session to delete.");
            return;
        }
        String sessionID = sessionTableModel.getValueAt(selectedRow, 0).toString();

        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this session?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                controller.deleteSession(sessionID, sessionTableModel);
                JOptionPane.showMessageDialog(null, "Session deleted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to delete session: " + ex.getMessage());
            }
        }
    }

    private void handleMarkAttendance(DefaultTableModel sessionAttendeesTableModel, DefaultTableModel attendeeTableModel) {
        String selectedSession = (String) sessionComboBox.getSelectedItem();
        if (selectedSession == null) {
            JOptionPane.showMessageDialog(this, "Please select a session.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sessionID = selectedSession.split(":")[0];
        int[] selectedRows = sessionAttendeesTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one attendee to mark attendance.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Map<String, Boolean> attendanceMap = new HashMap<>();
        for (int row : selectedRows) {
            String attendeeID = (String) sessionAttendeesTableModel.getValueAt(row, 0);
            Boolean attended = (Boolean) sessionAttendeesTableModel.getValueAt(row, 3);
            attendanceMap.put(attendeeID, attended);
        }

        try {
            controller.markAttendanceForSelectedSession(sessionID, new ArrayList<>(attendanceMap.keySet()), attendanceMap, sessionAttendeesTableModel);
            controller.loadAttendees(attendeeTableModel);
            JOptionPane.showMessageDialog(this, "Attendance marked successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred while marking attendance: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleIssueCertificates() {
        try {
            List<String> attendeeIDs = getDisplayedAttendeeIDs();
            if (attendeeIDs.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No attendees to process.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<String> issuedCertificates = controller.issueCertificatesForDisplayedAttendees(attendeeIDs);
            JOptionPane.showMessageDialog(this, issuedCertificates.size() + " certificates issued successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error issuing certificates: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleExportFeedbackReport() {
        String outputPath = "feedback_report.txt";
        try {
            controller.exportFeedbackReport(outputPath);
            JOptionPane.showMessageDialog(this,
                    "Feedback report exported successfully to: " + outputPath,
                    "Export Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error exporting feedback report: " + ex.getMessage(),
                    "Export Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            controller.logout();
            Login loginScreen = new Login();
            loginScreen.setVisible(true);
            this.dispose();
        }
    }

    private List<String> getDisplayedAttendeeIDs() {
        List<String> attendeeIDs = new ArrayList<>();
        for (int row = 0; row < attendeeTable.getRowCount(); row++) {
            String attendeeID = (String) attendeeTable.getValueAt(row, 0);
            attendeeIDs.add(attendeeID);
        }
        return attendeeIDs;
    }

    private void hideIDColumn(JTable table) {
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
    }
}
