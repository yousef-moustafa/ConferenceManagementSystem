package model.repository;

import model.domain.Session;
import model.domain.enums.SessionStatus;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class SessionRepository implements Repository<Session> {
    private ArrayList<Session> sessions = new ArrayList<>();
    private File file = new File("sessions.csv");

    // Load sessions from file when repository is initialized
    public SessionRepository() {
        loadFromFile();
    }

    @Override
    public void save(Session entity) {
        // Remove existing user with the same ID
        sessions.removeIf(session -> session.getSessionID().equals(entity.getSessionID()));

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
                String[] data = line.split(", ");
                if (data.length >= 8) {
                    Session session = new Session();

                    session.setSessionID(data[0]);
                    session.setSessionName(data[1]);
                    session.setSpeakerID(data[2]);
                    session.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(data[3]));
                    session.setTime(LocalTime.parse(data[4]));
                    session.setRoom(data[5]);
                    session.setCapacity(Integer.parseInt(data[6]));
                    session.setStatus(SessionStatus.valueOf(data[7].toUpperCase()));

                    // Parse attendee IDs (last field)
                    if (data.length > 8 && !data[8].isBlank()) {
                        List<String> attendeeIDs = Arrays.asList(data[8].split(" "));
                        session.getAttendeeIDs().addAll(attendeeIDs);
                    }

                    // Parse attendee attendance (if available)
                    if (data.length > 9 && !data[9].isBlank()) {
                        Map<String, Boolean> attendeeAttendance = Arrays.stream(data[9].split(";"))
                                .map(entry -> entry.split(":"))
                                .collect(Collectors.toMap(
                                        entry -> entry[0], // Attendee ID
                                        entry -> Boolean.parseBoolean(entry[1]) // Attendance status
                                ));
                        session.setAttendeeAttendance(attendeeAttendance);
                    }

                    sessions.add(session);
                }
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Error loading sessions from file: " + e.getMessage(), e);
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
