package view;

import model.domain.PersonalizedSchedule;
import model.dto.SessionDTO;
import model.dto.SpeakerDTO;
import model.service.AttendeeService;
import model.service.SessionService;
import model.service.SpeakerService;

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
        AttendeeService attendeeService = new AttendeeService(sessionService);

        loadConferenceSchedule(conferenceScheduleTableModel, sessionService, speakerService);
        loadPersonalizedSchedule(personalizedScheduleTableModel, attendeeID, attendeeService, sessionService, speakerService);

        // Update the status bar
        statusBar.setText("Logged in as: " + attendeeName + " | id: " + attendeeID);

        registerButton.addActionListener(e -> registerForSelectedSession(attendeeService, attendeeID));
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

}