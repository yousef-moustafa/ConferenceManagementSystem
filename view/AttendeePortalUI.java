package view;

import model.domain.PersonalizedSchedule;
import model.dto.CertificateDTO;
import model.dto.SessionDTO;
import model.dto.SpeakerDTO;
import model.service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AttendeePortalUI extends JFrame {
    private JPanel AttendeePortalUI;
    private JTabbedPane tabbedPane1;
    private JLabel statusBar;
    private JTable conferenceScheduleTable;
    private JButton registerButton;
    private JTable personalizedSchedule;
    private JButton unregisterButton;
    private JTextArea commentTextArea;
    private JButton clearFeedbackButton;
    private JSlider ratingSlider;
    private JButton submitFeedbackButton;
    private JLabel conferenceScheduleHeaderLabel;
    private JLabel myScheduleHeaderLabel;
    private JLabel certificateHeaderLabel;
    private JLabel noCertificateLabel;
    private JTextArea certificateDetails;

    public AttendeePortalUI(String attendeeID, String attendeeName) {
        setContentPane(AttendeePortalUI);
        setTitle("Attendee Portal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultTableModel conferenceScheduleTableModel = new DefaultTableModel(
                new String[]{"ID", "Title", "Speaker", "Speaker Bio", "Date", "Time", "Room"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        conferenceScheduleTable.setModel(conferenceScheduleTableModel);

        // Hide the ID and speaker bio column
        TableColumnModel conferenceColumnModel = conferenceScheduleTable.getColumnModel();
        conferenceColumnModel.getColumn(0).setMinWidth(0);
        conferenceColumnModel.getColumn(0).setMaxWidth(0);
        conferenceColumnModel.getColumn(0).setWidth(0);
        conferenceColumnModel.getColumn(3).setMinWidth(0);
        conferenceColumnModel.getColumn(3).setMaxWidth(0);
        conferenceColumnModel.getColumn(3).setWidth(0);

        // Create the table model for the personalized schedule
        DefaultTableModel personalizedScheduleTableModel = new DefaultTableModel(
                new String[]{"ID", "Title", "Speaker", "Speaker Bio", "Date", "Time", "Room"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        personalizedSchedule.setModel(personalizedScheduleTableModel);

        // Hide the ID column
        TableColumnModel scheduleColumnModel = personalizedSchedule.getColumnModel();
        scheduleColumnModel.getColumn(0).setMinWidth(0);
        scheduleColumnModel.getColumn(0).setMaxWidth(0);
        scheduleColumnModel.getColumn(0).setWidth(0);
        scheduleColumnModel.getColumn(3).setMinWidth(0);
        scheduleColumnModel.getColumn(3).setMaxWidth(0);
        scheduleColumnModel.getColumn(3).setWidth(0);

        SessionService sessionService = new SessionService();
        SpeakerService speakerService = new SpeakerService();
        FeedbackService feedbackService = new FeedbackService();

        AttendeeService attendeeService = new AttendeeService(sessionService, feedbackService);
        CertificateService certificateService = new CertificateService(attendeeService);

        loadConferenceSchedule(conferenceScheduleTableModel, sessionService, speakerService);
        loadPersonalizedSchedule(personalizedScheduleTableModel, attendeeID, attendeeService, sessionService, speakerService);
        initializeCertificateTab(certificateService, attendeeID, attendeeName);

        // Update the status bar
        statusBar.setText("Logged in as: " + attendeeName + " | id: " + attendeeID);

        conferenceScheduleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = conferenceScheduleTable.getSelectedRow();
                String speakerBio = conferenceScheduleTable.getValueAt(row, 3).toString(); // Speaker Bio column
                JOptionPane.showMessageDialog(null, speakerBio, "Speaker Bio", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        personalizedSchedule.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = personalizedSchedule.getSelectedRow();
                String speakerBio = personalizedSchedule.getValueAt(row, 3).toString(); // Speaker Bio column
                JOptionPane.showMessageDialog(null, speakerBio, "Speaker Bio", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            registerForSelectedSession(attendeeService, attendeeID);
            loadPersonalizedSchedule(personalizedScheduleTableModel, attendeeID, attendeeService, sessionService, speakerService);
        });
        unregisterButton.addActionListener(e -> {
            unregisterFromSelectedSession(attendeeService, attendeeID);
            loadPersonalizedSchedule(personalizedScheduleTableModel, attendeeID, attendeeService, sessionService, speakerService);
        });

        submitFeedbackButton.addActionListener(e -> submitFeedback(attendeeService, attendeeID));

        clearFeedbackButton.addActionListener(e -> {
            ratingSlider.setValue(3); // Reset slider to default
            commentTextArea.setText("");    // Clear comment area
            JOptionPane.showMessageDialog(this, "Feedback fields cleared.", "Info", JOptionPane.INFORMATION_MESSAGE);
        });

    }

    private void loadConferenceSchedule(DefaultTableModel tableModel, SessionService sessionService, SpeakerService speakerService) {
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

    private void submitFeedback(AttendeeService attendeeService, String attendeeID) {
        try {
            // Retrieve rating from the slider (mandatory)
            int rating = ratingSlider.getValue(); // Assume this is the JSlider instance

            // Retrieve comment from the text area (optional)
            String comment = commentTextArea.getText().trim(); // Assume this is the JTextArea instance

            // Submit rating feedback
            String ratingFeedbackID = attendeeService.submitRating(attendeeID, rating);

            // If a comment is provided, submit it as well
            if (!comment.isEmpty()) {
                String commentFeedbackID = attendeeService.submitComment(attendeeID, comment);
                JOptionPane.showMessageDialog(this, "Feedback submitted successfully!\n"
                        + "Rating ID: " + ratingFeedbackID + "\nComment ID: " + commentFeedbackID);
            } else {
                JOptionPane.showMessageDialog(this, "Rating submitted successfully!\nRating ID: " + ratingFeedbackID);
            }
        } catch (Exception ex) { // Catch generic exceptions
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Submission Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); // Optional: Log error details
        }
    }

    private void initializeCertificateTab(CertificateService certificateService, String attendeeID, String attendeeName) {
        try {
            // Attempt to fetch the certificate for the attendee
            CertificateDTO certificate = certificateService.getCertificateForAttendee(attendeeID);

            // If the certificate exists, display its details
            noCertificateLabel.setVisible(false);
            certificateDetails.setVisible(true);
            certificateDetails.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            certificateDetails.setLineWrap(false);
            certificateDetails.setWrapStyleWord(false);

            certificateDetails.setText(certificate.getDisplayString());
        } catch (IllegalArgumentException e) {
            // If no certificate is found, display the appropriate message
            noCertificateLabel.setVisible(true);
            certificateDetails.setVisible(false);

            noCertificateLabel.setText("<html>Manager has not sent you a certificate.<br>Make sure you have attended all sessions before the conference ends.</html>");
        }
    }

}