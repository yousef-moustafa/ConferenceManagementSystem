package view;

import model.domain.enums.SessionStatus;
import model.dto.SessionDTO;
import model.dto.SpeakerDTO;
import model.service.SessionService;
import model.service.SpeakerService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    private JTable table3;
    private JTextField textField1;
    private JButton searchButton;
    private JButton generateAttendanceReportButton;
    private JTable table4;
    private JButton exportFeedbackReportButton;

    public ManagerPortalUI() {
        setContentPane(ManagerPortalUI);
        setTitle("Manager Portal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize table models with ID columns
        DefaultTableModel speakerTableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Email", "Bio", "Associated Sessions"}, 0
        );
        speakerTable.setModel(speakerTableModel);

        DefaultTableModel sessionTableModel = new DefaultTableModel(
                new String[]{"ID", "Title", "Speaker", "Date", "Time", "Room", "Capacity", "Status"}, 0
        );
        sessionTable.setModel(sessionTableModel);

        SpeakerService speakerService = new SpeakerService();
        SessionService sessionService = new SessionService();

        loadSpeakers(speakerTableModel, speakerService);
        loadSessions(sessionTableModel, sessionService, speakerService);

        addSpeakerButton.addActionListener(e -> addSpeaker(speakerTableModel, speakerService));
        editSpeakerButton.addActionListener(e -> editSpeaker(speakerTableModel, speakerService));
        deleteSpeakerButton.addActionListener(e -> deleteSpeaker(speakerTableModel, speakerService));

        addSessionButton.addActionListener(e -> addSession(sessionTableModel, sessionService, speakerService));
    }

    private void loadSpeakers(DefaultTableModel speakerTableModel, SpeakerService speakerService) {
        speakerTableModel.setRowCount(0);

        List<SpeakerDTO> speakers = speakerService.getAllSpeakers();

        for (SpeakerDTO speaker : speakers) {
            speakerTableModel.addRow(new Object[]{
                    speaker.getSpeakerID(),
                    speaker.getName(),
                    speaker.getEmail(),
                    speaker.getBio(),
                    String.join(", ", speaker.getAssociatedSessionIDs())
            });
        }
    }

    private void loadSessions(DefaultTableModel sessionTableModel, SessionService sessionService, SpeakerService speakerService) {
        sessionTableModel.setRowCount(0);

        List<SessionDTO> sessions = sessionService.getAllSessions();

        for (SessionDTO session : sessions) {
            String speakerName = "";
            if (session.getSpeakerID() != null) {
                SpeakerDTO speaker = speakerService.getSpeakerProfile(session.getSpeakerID());
                if (speaker != null) {
                    speakerName = speaker.getName();
                }
            }
            sessionTableModel.addRow(new Object[]{
                    session.getSessionID(),
                    session.getSessionName(),
                    speakerName,
                    session.getDate(),
                    session.getTime(),
                    session.getRoom(),
                    session.getCapacity(),
                    session.getStatus().toString()
            });
        }
    }

    private void addSpeaker(DefaultTableModel speakerTableModel, SpeakerService speakerService) {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextArea bioArea = new JTextArea(5, 20);
        JScrollPane bioScrollPane = new JScrollPane(bioArea);

        Object[] fields = {"Name:", nameField, "Email:", emailField, "Password:", passwordField, "Bio:", bioScrollPane};

        int result = JOptionPane.showConfirmDialog(null, fields, "Add Speaker", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String bio = bioArea.getText().trim();

                SpeakerDTO newSpeaker = new SpeakerDTO();
                newSpeaker.setName(name);
                newSpeaker.setEmail(email);
                newSpeaker.setBio(bio);
                newSpeaker.setAssociatedSessionIDs(new ArrayList<>());

                String speakerID = speakerService.createSpeaker(newSpeaker, password);

                // Reload speakers to update the table
                loadSpeakers(speakerTableModel, speakerService);

                JOptionPane.showMessageDialog(null, "Speaker added successfully! ID: " + speakerID);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to add speaker: " + ex.getMessage());
            }
        }
    }

    private void editSpeaker(DefaultTableModel speakerTableModel, SpeakerService speakerService) {
        int selectedRow = speakerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a speaker to edit.");
            return;
        }

        String speakerID = speakerTableModel.getValueAt(selectedRow, 0).toString();

        SpeakerDTO speaker = speakerService.getSpeakerProfile(speakerID);
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
                String updatedName = nameField.getText().trim();
                String updatedBio = bioArea.getText().trim();

                speaker.setName(updatedName);
                speaker.setBio(updatedBio);

                speakerService.updateSpeakerProfile(speaker.getSpeakerID(), updatedName, updatedBio);

                // Reload speakers to update the table
                loadSpeakers(speakerTableModel, speakerService);

                JOptionPane.showMessageDialog(null, "Speaker updated successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to update speaker: " + ex.getMessage());
            }
        }
    }

    private void deleteSpeaker(DefaultTableModel speakerTableModel, SpeakerService speakerService) {
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
                speakerService.deleteSpeaker(speakerID);

                // Reload speakers to update the table
                loadSpeakers(speakerTableModel, speakerService);

                JOptionPane.showMessageDialog(null, "Speaker deleted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to delete speaker: " + ex.getMessage());
            }
        }
    }

    private void addSession(DefaultTableModel sessionTableModel, SessionService sessionService, SpeakerService speakerService) {
        JTextField sessionNameField = new JTextField();
        JTextField dateField = new JTextField("YYYY-MM-DD");
        JTextField timeField = new JTextField("HH:MM");
        JTextField roomField = new JTextField();
        JTextField capacityField = new JTextField();

        // Fetch speaker names and IDs
        List<SpeakerDTO> speakers = speakerService.getAllSpeakers();
        JComboBox<String> speakerComboBox = new JComboBox<>(speakers.stream()
                .map(SpeakerDTO::getName)
                .toArray(String[]::new));

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

                // Find the speaker ID by matching the name
                String speakerID = null;
                for (SpeakerDTO speaker : speakers) {
                    if (speaker.getName().equals(speakerName)) {
                        speakerID = speaker.getSpeakerID();
                        break;
                    }
                }

                if (speakerID == null) {
                    throw new Exception("Speaker not found.");
                }

                // Create a new SessionDTO
                SessionDTO newSession = new SessionDTO();
                newSession.setSessionName(name);
                newSession.setSpeakerID(speakerID);
                newSession.setDate(date);
                newSession.setTime(time);
                newSession.setRoom(room);
                newSession.setCapacity(capacity);
                newSession.setStatus(SessionStatus.SCHEDULED);

                // Save the new session via the service
                String sessionID = sessionService.createSession(newSession);
                newSession.setSessionID(sessionID);

                sessionTableModel.addRow(new Object[]{
                        sessionID,
                        newSession.getSessionName(),
                        speakerName,
                        newSession.getDate(),
                        newSession.getTime(),
                        newSession.getRoom(),
                        newSession.getCapacity(),
                        newSession.getStatus().toString()
                });

                JOptionPane.showMessageDialog(null, "Session added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to add session: " + ex.getMessage());
            }
        }
    }



}
