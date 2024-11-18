package model.repository;

import model.domain.Session;
import model.domain.enums.SessionStatus;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SessionRepository implements Repository<Session> {
    private ArrayList<Session> sessions = new ArrayList<>();
    private File file = new File("sessions.csv");

    // Load sessions from file when repository is initialized
    public SessionRepository() {
        loadFromFile();
    }

    @Override
    public void save(Session entity) {
        sessions.add(entity);
        writeToFile(); // Write to file after adding
    }

    @Override
    public Session findById(String id) {
        for (Session session : sessions) {
            if (session.getSessionID().equals(id)) {
                return session;
            }
        }
        return null; // Return null if session not found
    }

    @Override
    public List<Session> findAll() {
        return sessions;
    }

    @Override
    public void delete(String id) {
        sessions.removeIf(session -> session.getSessionID().equals(id));
        writeToFile(); // Write changes to file after deletion
    }

    public void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split line by comma
                String[] data = line.split(", ");

                if (data.length == 8) {
                    Session session = new Session();

                    // Directly set the values using setter methods
                    session.setSessionName(data[0]);
                    session.setSpeakerID(data[1]);
                    session.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(data[2]));
                    session.setTime(LocalTime.parse(data[3]));
                    session.setRoom(data[4]);
                    session.setStatus(SessionStatus.valueOf(data[5].toUpperCase()));
                    session.setCapacity(Integer.parseInt(data[6]));

                    // Add the session to the list
                    sessions.add(session);
                }
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // Write a session to the file
    private void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Session session : sessions) {
                writer.write(session.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
