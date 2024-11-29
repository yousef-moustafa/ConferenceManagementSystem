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

        // Add a pre-established Manager account
        String managerEmail = "manager@gaf.ac";
        if (findByEmail(managerEmail) == null) {
            User manager = new User(
                    "Nancy",
                    managerEmail,
                    "password123",
                    LocalDate.now(),
                    UserRole.MANAGER
            );
            save(manager); // Save the Manager account to the repository
        }
    }

    @Override
    public void save(User entity) {
        // Remove existing user with the same ID
        users.removeIf(user -> user.getUserID().equals(entity.getUserID()));

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
                String password = data[3];
                LocalDate registrationDate = LocalDate.parse(data[4]);
                UserRole role = UserRole.valueOf(data[5].toUpperCase());

                User user;
                if (role == UserRole.SPEAKER) {
                    String bio = data[6];
                    List<String> associatedSessionIDs = new ArrayList<>();
                    if (data.length > 7 && !data[7].isBlank()) {
                        associatedSessionIDs = Arrays.asList(data[7].split(" "));
                    }
                    user = new Speaker(userName, email, password, registrationDate, bio, associatedSessionIDs);
                    user.setUserID(userID);
                    users.add(user);
                } else if (role == UserRole.ATTENDEE) {
                    List<String> registeredSessionIDs = Arrays.asList(data[6].split(" "));
                    String personalizedScheduleID = data[7];
                    String certificateID = data[8];
                    String feedbackID = data[9];
                    user = new Attendee(userName, email, password, registrationDate, registeredSessionIDs, personalizedScheduleID, certificateID, feedbackID);
                    user.setUserID(userID);
                    users.add(user);
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

    public User findByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }
}