package view;

import model.domain.PersonalizedSchedule;
import model.dto.SessionDTO;
import model.dto.SpeakerDTO;
import model.service.AttendeeService;
import model.service.SessionService;
import model.service.SpeakerService;
import model.service.FeedbackService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class AttendeePortalUI extends JFrame {
    private JPanel AttendeePortalUI;
    private JTabbedPane tabbedPane1;
    private JLabel statusBar;
    private JTable conferenceScheduleTable;
    private JButton registerButton;
    private JTable personalizedSchedule;
    private JButton unregisterButton;
    private JTextArea textArea1;
    private JButton submitRatingButton;
    private JButton clearFeedbackButton;
    private JButton submitCommentButton;
    private JSlider ratingSlider;

    public AttendeePortalUI(String attendeeID, String attendeeName) {
        setContentPane(AttendeePortalUI);
        setTitle("Attendee Portal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultTableModel conferenceScheduleTableModel = new DefaultTableModel(
                new String[]{"ID", "Title", "Speaker", "Date", "Time", "Room"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        conferenceScheduleTable.setModel(conferenceScheduleTableModel);

        // Hide the ID column
        conferenceScheduleTable.getColumnModel().getColumn(0).setMinWidth(0);
        conferenceScheduleTable.getColumnModel().getColumn(0).setMaxWidth(0);
        conferenceScheduleTable.getColumnModel().getColumn(0).setPreferredWidth(0);


        // Create the table model for the personalized schedule
        DefaultTableModel personalizedScheduleTableModel = new DefaultTableModel(
                new String[]{"ID", "Title", "Speaker", "Date", "Time", "Room"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        personalizedSchedule.setModel(personalizedScheduleTableModel);

        // Hide the ID column
        personalizedSchedule.getColumnModel().getColumn(0).setMinWidth(0);
        personalizedSchedule.getColumnModel().getColumn(0).setMaxWidth(0);
        personalizedSchedule.getColumnModel().getColumn(0).setPreferredWidth(0);


        SessionService sessionService = new SessionService();
        SpeakerService speakerService = new SpeakerService();
        FeedbackService feedbackService = new FeedbackService();

        AttendeeService attendeeService = new AttendeeService(sessionService, feedbackService);

        loadConferenceSchedule(conferenceScheduleTableModel, sessionService, speakerService);
        loadPersonalizedSchedule(personalizedScheduleTableModel, attendeeID, attendeeService, sessionService, speakerService);

        // Update the status bar
        statusBar.setText("Logged in as: " + attendeeName + " | id: " + attendeeID);

        registerButton.addActionListener(e -> {
            registerForSelectedSession(attendeeService, attendeeID);
            loadPersonalizedSchedule(personalizedScheduleTableModel, attendeeID, attendeeService, sessionService, speakerService);
        });
        unregisterButton.addActionListener(e -> {
            unregisterFromSelectedSession(attendeeService, attendeeID);
            loadPersonalizedSchedule(personalizedScheduleTableModel, attendeeID, attendeeService, sessionService, speakerService);
        });

        submitRatingButton.addActionListener(e -> submitRating(attendeeService, attendeeID));
        submitCommentButton.addActionListener(e -> submitComment(attendeeService, attendeeID));

        clearFeedbackButton.addActionListener(e -> {
            ratingSlider.setValue(3); // Reset slider to default
            textArea1.setText("");    // Clear comment area
            JOptionPane.showMessageDialog(this, "Feedback fields cleared.", "Info", JOptionPane.INFORMATION_MESSAGE);
        });

    }

    private void loadConferenceSchedule(DefaultTableModel tableModel, SessionService sessionService, SpeakerService speakerService) {
        tableModel.setRowCount(0);

        List<SessionDTO> sessions = sessionService.getAllSessions();

        for (SessionDTO session : sessions) {
            String speakerName = "";
            if (session.getSpeakerID() != null) {
                SpeakerDTO speaker = speakerService.getSpeakerProfile(session.getSpeakerID());
                if (speaker != null) {
                    speakerName = speaker.getName();
                }
            }
            tableModel.addRow(new Object[]{
                    session.getSessionID(),
                    session.getSessionName(),
                    speakerName,
                    session.getDate(),
                    session.getTime(),
                    session.getRoom(),
            });
        }
    }

    private void registerForSelectedSession(AttendeeService attendeeService, String attendeeID) {
        int selectedRow = conferenceScheduleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a session to register.");
            return;
        }

        String sessionID = (String) conferenceScheduleTable.getModel().getValueAt(selectedRow, 0);

        boolean success = attendeeService.registerAttendeeForSession(attendeeID, sessionID);

        if (success) {
            JOptionPane.showMessageDialog(this, "You have successfully registered for the session!");
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. The session might already be in your schedule or conflicts with another session.");
        }
    }

    private void unregisterFromSelectedSession(AttendeeService attendeeService, String attendeeID) {
        int selectedRow = personalizedSchedule.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a session to unregister.");
            return;
        }

        // Get the session ID from the selected row in the table
        String sessionID = (String) personalizedSchedule.getModel().getValueAt(selectedRow, 0);

        // Call the unregister method in AttendeeService
        boolean success = attendeeService.unregisterAttendeeFromSession(attendeeID, sessionID);

        if (success) {
            JOptionPane.showMessageDialog(this, "You have successfully unregistered from the session.");
        } else {
            JOptionPane.showMessageDialog(this, "Unregistration failed. The session might not be in your schedule.");
        }
    }

    private void loadPersonalizedSchedule(DefaultTableModel tableModel, String attendeeID, AttendeeService attendeeService, SessionService sessionService, SpeakerService speakerService) {
        tableModel.setRowCount(0);

        // Get the personalized schedule for the attendee
        PersonalizedSchedule schedule = attendeeService.getAttendeeSchedule(attendeeID);

        if (schedule == null || schedule.getSessionsIDs().isEmpty()) {
            return;
        }

        // Iterate through each session ID in the personalized schedule
        for (String sessionID : schedule.getSessionsIDs()) {
            SessionDTO session = sessionService.getSession(sessionID);
            if (session != null) {
                String speakerName = "";
                if (session.getSpeakerID() != null) {
                    SpeakerDTO speaker = speakerService.getSpeakerProfile(session.getSpeakerID());
                    if (speaker != null) {
                        speakerName = speaker.getName();
                    }
                }
                tableModel.addRow(new Object[]{
                        session.getSessionID(),
                        session.getSessionName(),
                        speakerName,
                        session.getDate(),
                        session.getTime(),
                        session.getRoom()
                });
            }
        }
    }

    private void submitRating(AttendeeService attendeeService, String attendeeID) {
        try {
            int rating = ratingSlider.getValue();
            String feedbackID = attendeeService.submitRating(attendeeID, rating);
            JOptionPane.showMessageDialog(this, "Rating submitted successfully! Feedback ID: " + feedbackID, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error submitting rating: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void submitComment(AttendeeService attendeeService, String attendeeID) {
        try {
            String comment = textArea1.getText().trim(); // Get comment from text area
            if (comment.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Comment cannot be empty. Please provide feedback.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String feedbackID = attendeeService.submitComment(attendeeID, comment);
            JOptionPane.showMessageDialog(this, "Comment submitted successfully! Feedback ID: " + feedbackID, "Success", JOptionPane.INFORMATION_MESSAGE);

            textArea1.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error submitting comment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}