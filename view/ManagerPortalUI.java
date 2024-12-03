package view;

import model.domain.CommentFeedback;
import model.domain.Feedback;
import model.domain.FeedbackReport;
import model.domain.RatingFeedback;
import model.domain.enums.SessionStatus;
import model.dto.AttendeeDTO;
import model.dto.FeedbackDTO;
import model.dto.SessionDTO;
import model.dto.SpeakerDTO;
import model.repository.FeedbackRepository;
import model.repository.UserRepository;
import model.service.AttendeeService;
import model.service.FeedbackService;
import model.service.SessionService;
import model.service.SpeakerService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.text.SimpleDateFormat;
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
    private JTable attendeeTable;
    private JTextField searchField;
    private JButton searchButton;
    private JButton markAttendanceButton;
    private JTable feedbackTable;
    private JButton exportFeedbackReportButton;
    private JComboBox sessionComboBox;
    private JTable sessionAttendeesTable;
    private JLabel averageFeedbackLabel;

    public ManagerPortalUI() {
        setContentPane(ManagerPortalUI);
        setTitle("Manager Portal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize table models with ID columns
        DefaultTableModel speakerTableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Email", "Bio"}, 0
        );
        speakerTable.setModel(speakerTableModel);

        // Hide the ID column in the speaker table
        speakerTable.getColumnModel().getColumn(0).setMinWidth(0);
        speakerTable.getColumnModel().getColumn(0).setMaxWidth(0);
        speakerTable.getColumnModel().getColumn(0).setPreferredWidth(0);

        DefaultTableModel sessionTableModel = new DefaultTableModel(
                new String[]{"ID", "Title", "Speaker", "Date", "Time", "Room", "Capacity", "Status"}, 0
        );
        sessionTable.setModel(sessionTableModel);

        // Hide the ID column in the session table
        sessionTable.getColumnModel().getColumn(0).setMinWidth(0);
        sessionTable.getColumnModel().getColumn(0).setMaxWidth(0);
        sessionTable.getColumnModel().getColumn(0).setPreferredWidth(0);

        DefaultTableModel attendeeTableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Email", "Personalized Schedule ID"}, 0
        );
        attendeeTable.setModel(attendeeTableModel);

        // Hide the ID column in the attendee table
        attendeeTable.getColumnModel().getColumn(0).setMinWidth(0);
        attendeeTable.getColumnModel().getColumn(0).setMaxWidth(0);
        attendeeTable.getColumnModel().getColumn(0).setPreferredWidth(0);

        DefaultTableModel sessionAttendeesTableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Email"}, 0
        );
        sessionAttendeesTable.setModel(sessionAttendeesTableModel);

        DefaultTableModel feedbackTableModel = new DefaultTableModel(
                new String[]{"Feedback ID", "Attendee ID", "Type", "Details"}, 0
        );
        feedbackTable.setModel(feedbackTableModel);

        SpeakerService speakerService = new SpeakerService();
        SessionService sessionService = new SessionService();
        AttendeeService attendeeService = new AttendeeService(sessionService);
        FeedbackService feedbackService = new FeedbackService(new FeedbackRepository());

        loadSpeakers(speakerTableModel, speakerService);
        loadSessions(sessionTableModel, sessionService, speakerService);
        loadAttendees(attendeeTableModel, attendeeService);

        // Load sessions into combo box when tab is switched to process registration
        tabbedPane1.addChangeListener(e -> {
            if (tabbedPane1.getSelectedIndex() == 1) {
                loadSessionsIntoComboBox(sessionComboBox, sessionService);
            }
            if (tabbedPane1.getSelectedIndex() == 2) { // Assuming 3rd tab index is 2
                loadFeedbackTable(feedbackTableModel, feedbackService);
                updateAverageFeedbackLabel(averageFeedbackLabel, feedbackService);
            }
        });

        addSpeakerButton.addActionListener(e -> addSpeaker(speakerTableModel, speakerService));
        editSpeakerButton.addActionListener(e -> editSpeaker(speakerTableModel, speakerService));
        deleteSpeakerButton.addActionListener(e -> deleteSpeaker(speakerTableModel, speakerService));

        addSessionButton.addActionListener(e -> addSession(sessionTableModel, sessionService, speakerService));
        editSessionButton.addActionListener(e -> editSession(sessionTableModel, sessionService, speakerService));
        deleteSessionButton.addActionListener(e -> deleteSession(sessionTableModel, sessionService, speakerService));

        searchButton.addActionListener(e -> {
            if (searchField.getText().trim().isEmpty()) loadAttendees(attendeeTableModel, attendeeService);
            else searchAttendees(attendeeTableModel, attendeeService, searchField.getText().trim());
        });

        sessionComboBox.addActionListener(e -> {
            String selectedSession = (String) sessionComboBox.getSelectedItem();
            if (selectedSession != null) {
                String sessionID = selectedSession.split(":")[0]; // Extract session ID
                loadAttendeesForSession(sessionAttendeesTableModel, sessionService, sessionID);
            }
        });

        exportFeedbackReportButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Export functionality is under development.");
        });
    }

    private void loadSpeakers(DefaultTableModel speakerTableModel, SpeakerService speakerService) {
        speakerTableModel.setRowCount(0);

        List<SpeakerDTO> speakers = speakerService.getAllSpeakers();
        for (SpeakerDTO speaker : speakers) {
            speakerTableModel.addRow(new Object[]{
                    speaker.getSpeakerID(),
                    speaker.getName(),
                    speaker.getEmail(),
                    speaker.getBio()
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

    private void loadAttendees(DefaultTableModel attendeeTableModel, AttendeeService attendeeService) {
        attendeeTableModel.setRowCount(0);

        List<AttendeeDTO> attendees = attendeeService.getAllAttendees();
        for (AttendeeDTO attendee : attendees) {
            attendeeTableModel.addRow(new Object[]{
                    attendee.getAttendeeID(),
                    attendee.getName(),
                    attendee.getEmail(),
                    attendee.getPersonalizedScheduleID()
            });
        }
    }

    private void loadFeedbackTable(DefaultTableModel feedbackTableModel, FeedbackService feedbackService) {
        feedbackTableModel.setRowCount(0);

        List<FeedbackDTO> feedbackDTOs = feedbackService.getAllFeedback();
        for (FeedbackDTO feedback : feedbackDTOs) {
            feedbackTableModel.addRow(new Object[]{
                    feedback.getFeedbackID(),
                    feedback.getAttendeeID(),
                    feedback.getType(),
                    feedback.getDetails()
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
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, "Failed to add session: " + ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + ex.getMessage());
            }
        }
    }

    private void editSession(DefaultTableModel sessionTableModel, SessionService sessionService, SpeakerService speakerService) {
        int selectedRow = sessionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a session to edit.");
            return;
        }

        String sessionID = sessionTableModel.getValueAt(selectedRow, 0).toString();

        SessionDTO session = sessionService.getSession(sessionID);
        if (session == null) {
            JOptionPane.showMessageDialog(null, "Session not found.");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(session.getDate());

        JTextField sessionNameField = new JTextField(session.getSessionName());
        JTextField dateField = new JTextField(formattedDate); // Use formatted date
        JTextField timeField = new JTextField(session.getTime().toString());
        JTextField roomField = new JTextField(session.getRoom());
        JTextField capacityField = new JTextField(String.valueOf(session.getCapacity()));

        // Fetch speaker names and IDs
        List<SpeakerDTO> speakers = speakerService.getAllSpeakers();
        JComboBox<String> speakerComboBox = new JComboBox<>(speakers.stream()
                .map(SpeakerDTO::getName)
                .toArray(String[]::new));

        // Set the selected speaker in the combo box
        SpeakerDTO currentSpeaker = speakerService.getSpeakerProfile(session.getSpeakerID());
        if (currentSpeaker != null) {
            speakerComboBox.setSelectedItem(currentSpeaker.getName());
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
                String updatedName = sessionNameField.getText().trim();
                Date updatedDate = Date.valueOf(dateField.getText().trim());
                LocalTime updatedTime = LocalTime.parse(timeField.getText().trim());
                String updatedRoom = roomField.getText().trim();
                int updatedCapacity = Integer.parseInt(capacityField.getText().trim());
                String updatedSpeakerName = (String) speakerComboBox.getSelectedItem();

                // Find the updated speaker ID by matching the name
                String updatedSpeakerID = null;
                for (SpeakerDTO speaker : speakers) {
                    if (speaker.getName().equals(updatedSpeakerName)) {
                        updatedSpeakerID = speaker.getSpeakerID();
                        break;
                    }
                }

                if (updatedSpeakerID == null) {
                    throw new Exception("Speaker not found.");
                }

                // Update the session DTO
                session.setSessionName(updatedName);
                session.setDate(updatedDate);
                session.setTime(updatedTime);
                session.setRoom(updatedRoom);
                session.setCapacity(updatedCapacity);
                session.setSpeakerID(updatedSpeakerID);

                // Save the updated session
                sessionService.updateSession(session);

                // Reload sessions to reflect changes
                loadSessions(sessionTableModel, sessionService, speakerService);

                JOptionPane.showMessageDialog(null, "Session updated successfully!");
            } catch (Exception ex) {
                ex.printStackTrace(); // This will show the full stack trace in the console
                JOptionPane.showMessageDialog(null, "Failed to update session: " + ex.getMessage());
            }
        }
    }

    private void deleteSession(DefaultTableModel sessionTableModel, SessionService sessionService, SpeakerService speakerService) {
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
                sessionService.deleteSession(sessionID);

                // Reload sessions to update the table
                loadSessions(sessionTableModel, sessionService, speakerService);

                JOptionPane.showMessageDialog(null, "Session deleted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to delete session: " + ex.getMessage());
            }
        }
    }

    private void searchAttendees(DefaultTableModel attendeeTableModel, AttendeeService attendeeService, String query) {
        attendeeTableModel.setRowCount(0);

        List<AttendeeDTO> filteredAttendees = attendeeService.searchAttendees(query);
        for (AttendeeDTO attendee : filteredAttendees) {
            attendeeTableModel.addRow(new Object[]{
                    attendee.getAttendeeID(),
                    attendee.getName(),
                    attendee.getEmail(),
                    attendee.getPersonalizedScheduleID()
            });
        }
    }

    private void loadSessionsIntoComboBox(JComboBox<String> sessionComboBox, SessionService sessionService) {
        sessionComboBox.removeAllItems();

        List<SessionDTO> sessions = sessionService.getAllSessions(); // Fetch all sessions
        for (SessionDTO session : sessions) {
            sessionComboBox.addItem(session.getSessionID() + ": " + session.getSessionName());
        }
    }

    private void loadAttendeesForSession(DefaultTableModel sessionAttendeesTableModel, SessionService sessionService, String sessionID) {
        sessionAttendeesTableModel.setRowCount(0);

        List<AttendeeDTO> attendees = sessionService.getSessionAttendees(sessionID);
        for (AttendeeDTO attendee : attendees) {
            sessionAttendeesTableModel.addRow(new Object[]{
                    attendee.getAttendeeID(),
                    attendee.getName(),
                    attendee.getEmail()
            });
        }
    }

    private void updateAverageFeedbackLabel(JLabel averageFeedbackLabel, FeedbackService feedbackService) {
        FeedbackReport report = feedbackService.getConferenceFeedbackAnalysis();
        String averageRating = String.format("%.2f", report.getAverageRating());
        int totalResponses = report.getTotalResponses();

        averageFeedbackLabel.setText("Total Responses: " + totalResponses + " | Average Feedback Rating: " + averageRating);
    }

}
