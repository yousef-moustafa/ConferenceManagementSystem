package view;

import controller.AttendeeController;
import controller.ManagerController;
import main.ApplicationContext;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    private JTextPane certificateDetails;
    private JButton logoutButton;
    private JLabel logoLabel;

    private final AttendeeController controller;

    public AttendeePortalUI(String attendeeID, String attendeeName) {
        this.controller = ApplicationContext.getInstance().getAttendeeController();

        setContentPane(AttendeePortalUI);
        setTitle("Attendee Portal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Setup table models
        DefaultTableModel conferenceScheduleTableModel = new DefaultTableModel(
                new String[]{"ID", "Title", "Speaker", "Speaker Bio", "Date", "Time", "Room"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        conferenceScheduleTable.setModel(conferenceScheduleTableModel);
        hideColumns(conferenceScheduleTable, 0, 3);

        DefaultTableModel personalizedScheduleTableModel = new DefaultTableModel(
                new String[]{"ID", "Title", "Speaker", "Speaker Bio", "Date", "Time", "Room"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        personalizedSchedule.setModel(personalizedScheduleTableModel);
        hideColumns(personalizedSchedule, 0, 3);

        // Load data
        controller.loadConferenceSchedule(conferenceScheduleTableModel);
        controller.loadPersonalizedSchedule(personalizedScheduleTableModel, attendeeID);
        controller.initializeCertificateTab(noCertificateLabel, certificateDetails, attendeeID);

        // Update status bar
        statusBar.setText("Logged in as: " + attendeeName + " | id: " + attendeeID);

        // Add event listeners
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
            controller.registerForSession(conferenceScheduleTableModel, conferenceScheduleTable, attendeeID);
            controller.loadPersonalizedSchedule(personalizedScheduleTableModel, attendeeID);
        });

        unregisterButton.addActionListener(e -> {
            controller.unregisterFromSession(personalizedScheduleTableModel, personalizedSchedule, attendeeID);
            controller.loadPersonalizedSchedule(personalizedScheduleTableModel, attendeeID);
        });

        submitFeedbackButton.addActionListener(e -> {
            int rating = ratingSlider.getValue();
            String comment = commentTextArea.getText().trim();
            controller.submitFeedback(attendeeID, rating, comment);
        });

        clearFeedbackButton.addActionListener(e -> {
            ratingSlider.setValue(3); // Reset slider to default
            commentTextArea.setText("");    // Clear comment area
            JOptionPane.showMessageDialog(this, "Feedback fields cleared.", "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        logoutButton.addActionListener(e -> controller.handleLogout(this));
    }

    private void hideColumns(JTable table, int... columns) {
        for (int column : columns) {
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(column).setMinWidth(0);
            columnModel.getColumn(column).setMaxWidth(0);
            columnModel.getColumn(column).setWidth(0);
        }
    }
}
