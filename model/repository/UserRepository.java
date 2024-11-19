package model.repository;

import model.domain.User;
import model.domain.Speaker;
import model.domain.Attendee;
import model.domain.enums.UserRole;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserRepository implements Repository<User> {
    private List<User> users = new ArrayList<>();
    private File file = new File("users.csv");

    public UserRepository() {
        loadFromFile();
    }

    @Override
    public void save(User entity) {
        users.add(entity);
        writeToFile();
    }

    @Override
    public User findById(String id) {
        for (User user : users) {
            if (user.getUserID().equals(id)) {
                return user;
            }
        }
        return null; // Return null if user not found
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public void delete(String id) {
        users.removeIf(user -> user.getUserID().equals(id));
        writeToFile();
    }

    public void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");

                String userID = data[0];
                String userName = data[1];
                String email = data[2];
                LocalDate registrationDate = LocalDate.parse(data[3]);
                UserRole role = UserRole.valueOf(data[4].toUpperCase());

                // Create user based on role
                if (role == UserRole.SPEAKER) {
                    String bio = data[5];
                    List<String> associatedSessionIDs = Arrays.asList(data[6].split(" ")); // Assumed sessions are space-separated
                    users.add(new Speaker(userID, userName, email, registrationDate, bio, associatedSessionIDs));
                } else if (role == UserRole.ATTENDEE) {
                    List<String> registeredSessionIDs = Arrays.asList(data[5].split(" "));
                    String personalizedScheduleID = data[6];
                    String certificateID = data[7];
                    String feedbackID = data[8];
                    users.add(new Attendee(userID, userName, email, registrationDate, registeredSessionIDs, personalizedScheduleID, certificateID, feedbackID));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (User user : users) {
                writer.write(user.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}