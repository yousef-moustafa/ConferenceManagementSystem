package view;

import javax.swing.*;

public class ManagerPortalUI extends JFrame {
    public ManagerPortalUI() {
        setTitle("Manager Portal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("Welcome to the Manager Portal!", SwingConstants.CENTER);
        add(label);
    }
}