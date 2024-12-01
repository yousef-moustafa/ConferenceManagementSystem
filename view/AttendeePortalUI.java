package view;

import javax.swing.*;

public class AttendeePortalUI extends JFrame {
    public AttendeePortalUI(String attendeeID, String attendeeName) {
        setTitle("Attendee Portal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("Welcome to the Attendee Portal!", SwingConstants.CENTER);
        add(label);
    }
}