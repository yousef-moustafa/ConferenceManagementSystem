package view;

import javax.swing.*;

public class Login extends JFrame {
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JLabel errorMessage;
    private JPanel Login;

    public Login() {
        setContentPane(Login);
        setTitle("Conference Management System Login");
        setSize(450,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        Login login = new Login();
    }
}
