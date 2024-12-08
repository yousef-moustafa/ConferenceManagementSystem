package view;

import controller.SpeakerController;
import model.repository.UserRepository;
import model.service.AuthService;
import model.service.SessionService;
import model.service.SpeakerService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class SpeakerPortalUI extends JFrame {
    private JPanel SpeakerPortalUI;
    private JTabbedPane tabbedPane1;
    private JTable speakerSessionsTable;
    private JButton viewSessionDetailsButton;
    private JTextArea speakerBio;
    private JButton saveBioButton;
    private JLabel statusBar;
    private JButton logoutButton;

    private final SpeakerController controller;

    public SpeakerPortalUI(String speakerID, String speakerName) {
        setContentPane(SpeakerPortalUI);
        setTitle("Speaker Portal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize services and controller
        SpeakerService speakerService = new SpeakerService();
        SessionService sessionService = new SessionService();
        UserRepository userRepository = new UserRepository();
        AuthService authService = new AuthService(userRepository);
        controller = new SpeakerController(speakerService, sessionService, authService);

        // Setup table model
        DefaultTableModel speakerSessionTableModel = new DefaultTableModel(
                new String[]{"ID", "Session Title"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        speakerSessionsTable.setModel(speakerSessionTableModel);
        hideIDColumn();

        // Load data
        controller.loadSpeakerSessions(speakerSessionTableModel, speakerID);
        controller.loadSpeakerBio(speakerID, speakerBio);

        // Update status bar
        statusBar.setText("Logged in as: " + speakerName + " | id: " + speakerID);

        // Add event listeners
        viewSessionDetailsButton.addActionListener(e -> controller.viewSessionDetails(speakerSessionTableModel, speakerSessionsTable));
        saveBioButton.addActionListener(e -> controller.saveSpeakerBio(speakerID, speakerBio));
        logoutButton.addActionListener(e -> controller.handleLogout(this));
    }

    private void hideIDColumn() {
        speakerSessionsTable.getColumnModel().getColumn(0).setMinWidth(0);
        speakerSessionsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        speakerSessionsTable.getColumnModel().getColumn(0).setPreferredWidth(0);
    }
}
