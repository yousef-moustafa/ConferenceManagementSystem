package view;

import main.ApplicationContext;
import model.dto.AttendeeDTO;
import model.service.AttendeeService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AttendeeRegistrationUI extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JLabel errorMessage;

    private JPanel AttendeeRegistrationUI;
    private final AttendeeService attendeeService;

    public AttendeeRegistrationUI() {
        this.attendeeService = ApplicationContext.getInstance().getAttendeeService();

        // Setup UI
        setContentPane(AttendeeRegistrationUI);
        setTitle("Attendee Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Register button logic
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistration();
            }
        });
    }

    private void handleRegistration() {
        // Capture input
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        // Reset error message
        errorMessage.setText("");

        // Validate input
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            errorMessage.setText("All fields are required.");
            return;
        }

        if (!isValidEmail(email)) {
            errorMessage.setText("Invalid email format.");
            return;
        }

        // Create AttendeeDTO
        String personalizedScheduleID = ""; // No schedule at registration
        List<String> attendedSessions = new ArrayList<>(); // Attendee did not attend any sessions at registration
        AttendeeDTO attendeeDTO = new AttendeeDTO(null, name, email, personalizedScheduleID, attendedSessions);

        try {
            // Create attendee using the service
            String attendeeID = attendeeService.createAttendee(attendeeDTO, password);
            attendeeDTO.setAttendeeID(attendeeID);

            // Show success message
            JOptionPane.showMessageDialog(this, "Registration successful! Your ID: " + attendeeID);

            // Redirect back
            new Login().setVisible(true);
            this.dispose();
        } catch (Exception ex) {
            errorMessage.setText(ex.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }
}

