package view;

import javax.swing.*;
import java.awt.*;

public class RedirectUI {
    JFrame frame = new JFrame();
    JLabel welcomeLabel = new JLabel("Welcome to model.domain.Conference!");

    RedirectUI(String userID){
        welcomeLabel.setBounds(0,0,290,50);
        welcomeLabel.setFont(new Font(null, Font.PLAIN,25));
        welcomeLabel.setText("Hello " + userID);

        frame.add(welcomeLabel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,420);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
