package view;

import model.dto.SpeakerDTO;
import model.service.SpeakerService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class ManagerPortalUI extends JFrame {
    private JPanel ManagerPortalUI;
    private JTabbedPane tabbedPane1;
    private JTable speakerTable;
    private JButton addSpeakerButton;
    private JButton editSpeakerButton;
    private JButton deleteSpeakerButton;
    private JTable table1;
    private JButton addSessionButton;
    private JButton editSessionButton;
    private JButton deleteSessionButton;
    private JTable table3;
    private JTextField textField1;
    private JButton searchButton;
    private JButton generateAttendanceReportButton;
    private JTable table4;
    private JButton exportFeedbackReportButton;

    // Class-level list to store speaker IDs
    private List<String> speakerIDs = new ArrayList<>();

    public ManagerPortalUI() {
        setContentPane(ManagerPortalUI);
        setTitle("Manager Portal");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultTableModel speakerTableModel = new DefaultTableModel(
                new String[]{"Name", "Email", "Bio", "Associated Sessions"}, 0
        );
        speakerTable.setModel(speakerTableModel);

        SpeakerService speakerService = new SpeakerService();

        // Load speakers into the table
        loadSpeakers(speakerTableModel, speakerService);

        addSpeakerButton.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField passwordField = new JPasswordField();
            JTextArea bioArea = new JTextArea(5, 20);
            JScrollPane bioScrollPane = new JScrollPane(bioArea);

            Object[] fields = {
                    "Name:", nameField,
                    "Email:", emailField,
                    "Password:", passwordField,
                    "Bio:", bioScrollPane
            };

            int result = JOptionPane.showConfirmDialog(
                    null, fields, "Add Speaker", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String name = nameField.getText().trim();
                    String email = emailField.getText().trim();
                    String password = passwordField.getText().trim();
                    String bio = bioArea.getText().trim();

                    SpeakerDTO newSpeaker = new SpeakerDTO(name, email, bio, new ArrayList<>());
                    String speakerID = speakerService.createSpeaker(newSpeaker, password);

                    speakerIDs.add(speakerID); // Add the ID to the list
                    speakerTableModel.addRow(new Object[]{
                            name,
                            email,
                            bio,
                            "None"
                    });

                    JOptionPane.showMessageDialog(null, "Speaker added successfully! ID: " + speakerID);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Failed to add speaker: " + ex.getMessage());
                }
            }
        });

        editSpeakerButton.addActionListener(e -> {
            int selectedRow = speakerTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please select a speaker to edit.");
                return;
            }

            String speakerID = speakerIDs.get(selectedRow); // Get the ID from the list
            String name = (String) speakerTable.getValueAt(selectedRow, 0);
            String bio = (String) speakerTable.getValueAt(selectedRow, 2);

            JTextField nameField = new JTextField(name);
            JTextArea bioArea = new JTextArea(bio, 5, 20);
            JScrollPane bioScrollPane = new JScrollPane(bioArea);

            Object[] fields = {
                    "Name:", nameField,
                    "Bio:", bioScrollPane
            };

            int result = JOptionPane.showConfirmDialog(
                    null, fields, "Edit Speaker", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String updatedName = nameField.getText().trim();
                    String updatedBio = bioArea.getText().trim();

                    speakerTable.setValueAt(updatedName, selectedRow, 0);
                    speakerTable.setValueAt(updatedBio, selectedRow, 2);

                    speakerService.updateSpeakerBio(speakerID, updatedBio);

                    JOptionPane.showMessageDialog(null, "Speaker updated successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Failed to update speaker: " + ex.getMessage());
                }
            }
        });

        deleteSpeakerButton.addActionListener(e -> {
            int selectedRow = speakerTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please select a speaker to delete.");
                return;
            }

            String speakerID = speakerIDs.get(selectedRow);

            int result = JOptionPane.showConfirmDialog(
                    null, "Are you sure you want to delete this speaker?", "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                try {
                    speakerService.deleteSpeaker(speakerID);

                    speakerIDs.remove(selectedRow); // Remove the ID from the list
                    ((DefaultTableModel) speakerTable.getModel()).removeRow(selectedRow);

                    JOptionPane.showMessageDialog(null, "Speaker deleted successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Failed to delete speaker: " + ex.getMessage());
                }
            }
        });
    }

    private void loadSpeakers(DefaultTableModel speakerTableModel, SpeakerService speakerService) {
        speakerTableModel.setRowCount(0);
        speakerIDs.clear();

        List<SpeakerDTO> speakers = speakerService.getAllSpeakersWithIDs(speakerIDs);

        for (SpeakerDTO speaker : speakers) {
            speakerTableModel.addRow(new Object[]{
                    speaker.getName(),
                    speaker.getEmail(),
                    speaker.getBio(),
                    String.join(", ", speaker.getAssociatedSessionIDs())
            });
        }
    }
}
