package view;

import javax.swing.*;

public class SpeakerPortalUI extends JFrame {
    public SpeakerPortalUI() {
        setTitle("Speaker Portal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("Welcome to the Speaker Portal!", SwingConstants.CENTER);
        add(label);
    }
}