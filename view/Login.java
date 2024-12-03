package view;

import model.domain.User;
import model.domain.enums.AuthResult;
import model.domain.enums.UserRole;
import model.service.AttendeeService;
import model.service.AuthService;
import model.repository.UserRepository;
import model.service.FeedbackService;
import model.service.SessionService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JLabel errorMessage;
    private JPanel Login;
    private JRadioButton adminRadioButton;
    private JRadioButton attendeeRadioButton;
    private JRadioButton speakerRadioButton;
    private JButton loginButton;
    private JButton registerButton;

    private final AuthService authService;

    public Login() {

        // Initialize authService with a UserRepository
        UserRepository userRepository = new UserRepository();
        this.authService = new AuthService(userRepository);

        // Setup UI
        setContentPane(Login);
        setTitle("Conference Management System Login");
        setSize(450,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        // ActionListener to Login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Step 1: Capture user input
                String email = textField1.getText();
                String password = new String(passwordField1.getPassword());
                UserRole role = getSelectedRole();

                // Step 2: Validate input
                if (email.isEmpty() || password.isEmpty() || role == null) {
                    errorMessage.setText("Please fill in all fields and select a role.");
                    return;
                }

                // Step 3: Authenticate user
                AuthResult authResult = authService.login(email, password);

                // Step 4: Handle authentication result
                handleAuthResult(authResult, role);
            }
        });

        // Action listener to Register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create necessary services
                SessionService sessionService = new SessionService();
                FeedbackService feedbackService = new FeedbackService();
                AttendeeService attendeeService = new AttendeeService(sessionService, feedbackService);

                JFrame register = new AttendeeRegistrationUI(attendeeService);
                register.setVisible(true);
                register.setLocationRelativeTo(null);
                Login.this.dispose();
            }
        });

        // Add a ChangeListener to the ButtonGroup
        ActionListener buttonGroupListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Enable the Register button only if attendeeRadioButton is selected
                registerButton.setEnabled(attendeeRadioButton.isSelected());
            }
        };

        // Attach the same ActionListener to all radio buttons
        adminRadioButton.addActionListener(buttonGroupListener);
        attendeeRadioButton.addActionListener(buttonGroupListener);
        speakerRadioButton.addActionListener(buttonGroupListener);

    }

    private UserRole getSelectedRole() {
        if (adminRadioButton.isSelected()) return UserRole.MANAGER;
        if (attendeeRadioButton.isSelected()) return UserRole.ATTENDEE;
        if (speakerRadioButton.isSelected()) return UserRole.SPEAKER;
        return null; // No role selected
    }

    private void handleAuthResult(AuthResult result, UserRole role) {
        if (result == AuthResult.SUCCESS) {
            User currentUser = authService.getCurrentUser();
            if (currentUser.getUserRole() != role) {
                JOptionPane.showMessageDialog(this, "Role mismatch. Please select the correct role.");
                return;
            }

            // Navigate to the correct dashboard
            navigateToDashboard(role);
        } else {
            String errorMessage = switch (result) {
                case USER_NOT_FOUND -> "User not found.";
                case INVALID_CREDENTIALS -> "Invalid credentials. Please try again.";
                default -> "System error. Please contact support.";
            };
            JOptionPane.showMessageDialog(this, errorMessage);
        }
    }

    private void navigateToDashboard(UserRole role) {
        User currentUser = authService.getCurrentUser();
        JFrame dashboard;
        switch (role) {
            case MANAGER:
                dashboard = new ManagerPortalUI();
                break;
            case SPEAKER:
                dashboard = new SpeakerPortalUI(currentUser.getUserID(), currentUser.getUserName());
                break;
            case ATTENDEE:
                dashboard = new AttendeePortalUI(currentUser.getUserID(), currentUser.getUserName());
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unknown role.");
                return;
        }
        dashboard.setVisible(true);
        dashboard.setLocationRelativeTo(null);
        this.dispose(); // Close the login window
    }

    public static void main(String[] args) {
        Login login = new Login();
        login.setLocationRelativeTo(null);
    }
}
