package model.service;

import model.domain.Attendee;
import model.domain.PersonalizedSchedule;
import model.domain.User;
import model.dto.AttendeeDTO;
import model.dto.DTOMapper;
import model.repository.UserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AttendeeService {
    private final UserRepository userRepository;
    private final SessionService sessionService; // To handle cross-references between attendees and sessions
    private final List<PersonalizedSchedule> schedules; // Temporary in-memory storage for schedules

    private final FeedbackService feedbackService;

    public AttendeeService(UserRepository userRepository, SessionService sessionService, FeedbackService feedbackService) {
        this.userRepository = userRepository;
        this.sessionService = sessionService;
        this.feedbackService = feedbackService;
        this.schedules = new ArrayList<>();
        loadSchedulesFromFile();
    }

    // Create a new attendee
    public String createAttendee(AttendeeDTO attendeeDTO, String password) throws Exception {
        // Check for duplicate email
        if (userRepository.findByEmail(attendeeDTO.getEmail()) != null) {
            throw new Exception("Email is already registered.");
        }

        Attendee attendee = DTOMapper.mapDTOToAttendee(attendeeDTO, password);

        PersonalizedSchedule schedule = new PersonalizedSchedule(attendee.getUserID(), new ArrayList<>());
        schedules.add(schedule);
        attendee.setPersonalizedScheduleID(schedule.getScheduleID());

        userRepository.save(attendee);
        saveSchedulesToFile();
        System.out.println(attendee.getUserID());
        return attendee.getUserID(); // Return the new attendee's ID
    }

    // Get an attendee's profile
    public AttendeeDTO getAttendeeProfile(String attendeeID) {
        Attendee attendee = (Attendee) userRepository.findById(attendeeID);
        return attendee != null ? DTOMapper.mapAttendeeToDTO(attendee) : null;
    }

    // Get an attendee's personalized schedule
    public PersonalizedSchedule getAttendeeSchedule(String attendeeID) {
        return schedules.stream()
                .filter(schedule -> schedule.getAttendeeID().equals(attendeeID))
                .findFirst()
                .orElse(null);
    }

    // Register an attendee for a session
    public boolean registerAttendeeForSession(String attendeeID, String sessionID) {
        PersonalizedSchedule schedule = getAttendeeSchedule(attendeeID);
        if (schedule != null) {
            if (schedule.addSession(sessionID)) {
                sessionService.addAttendeeToSession(sessionID, attendeeID); // Ensure session reflects the change
                saveSchedulesToFile();
                return true; // Registration successful
            }
        }
        return false; // Registration failed
    }

    // Unregister an attendee from a session
    public boolean unregisterAttendeeFromSession(String attendeeID, String sessionID) {
        PersonalizedSchedule schedule = getAttendeeSchedule(attendeeID);
        if (schedule != null) {
            if (schedule.removeSession(sessionID)) {
                sessionService.removeAttendeeFromSession(sessionID, attendeeID);
                saveSchedulesToFile();
                return true; // Unregistration successful
            }
        }
        return false; // Unregistration failed
    }

    // Get all attendees
    public List<AttendeeDTO> getAllAttendees() {
        List<User> users = userRepository.findAll();
        List<AttendeeDTO> attendees = new ArrayList<>();

        for (User user : users) {
            if (user instanceof Attendee) {
                Attendee attendee = (Attendee) user;
                attendees.add(DTOMapper.mapAttendeeToDTO(attendee));
            }
        }
        return attendees;
    }

    // Query attendee by name or email
    public List<AttendeeDTO> searchAttendees(String query) {
        return getAllAttendees().stream()
                .filter(attendee -> attendee.getName().toLowerCase().contains(query.toLowerCase()) ||
                        attendee.getEmail().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    private void loadSchedulesFromFile() {
        File file = new File("data/schedules.txt");
        if (!file.exists()) return; // Skip loading if the file doesn't exist

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                String scheduleID = data[0];
                String attendeeID = data[1];
                List<String> sessionIDs = new ArrayList<>();
                if (data.length > 2 && !data[2].isBlank()) {
                    sessionIDs = new ArrayList<>(Arrays.asList(data[2].split(" ")));
                }
                PersonalizedSchedule schedule = new PersonalizedSchedule(attendeeID, sessionIDs);
                schedule.setScheduleID(scheduleID); // Set the ID explicitly
                schedules.add(schedule);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveSchedulesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/schedules.txt"))) {
            for (PersonalizedSchedule schedule : schedules) {
                writer.write(schedule.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String submitRating(String attendeeID, int rating) throws Exception {
        Attendee attendee = (Attendee) userRepository.findById(attendeeID);
        if (attendee == null) {
            throw new Exception("Attendee with ID " + attendeeID + " not found.");
        }

        String feedbackID = feedbackService.submitRating(attendeeID, rating);
        attendee.setRatingFeedbackID(feedbackID);
        userRepository.save(attendee);

        return feedbackID;
    }

    public String submitComment(String attendeeID, String comment) throws Exception {
        Attendee attendee = (Attendee) userRepository.findById(attendeeID);
        if (attendee == null) {
            throw new Exception("Attendee with ID " + attendeeID + " not found.");
        }

        String feedbackID = feedbackService.submitComment(attendeeID, comment);
        attendee.setCommentFeedbackID(feedbackID);
        userRepository.save(attendee);

        return feedbackID;
    }

    // Method to Update Attendee Attendance
    public void updateAttendeeAttendance(String attendeeID, String sessionID, boolean attended) {
        Attendee attendee = (Attendee) userRepository.findById(attendeeID);
        if (attendee == null) {
            throw new IllegalArgumentException("Attendee with ID " + attendeeID + " not found.");
        }

        if (attended) {
            attendee.getAttendedSessions().add(sessionID);
        } else {
            attendee.getAttendedSessions().remove(sessionID);
        }

        userRepository.save(attendee);
    }

    // Update the certificateID for an attendee
    public void updateAttendeeCertificateID(String attendeeID, String certificateID) {
        Attendee attendee = (Attendee) userRepository.findById(attendeeID);
        if (attendee == null) {
            throw new IllegalArgumentException("Attendee with ID " + attendeeID + " not found.");
        }

        attendee.setCertificateID(certificateID);
        userRepository.save(attendee); // Persist the updated attendee
    }

    // Mark attendance for an attendee in a session
    public void markAttendance(String attendeeID, String sessionID, boolean attended) {
        // Delegate session-level attendance update
        sessionService.markAttendance(sessionID, attendeeID, attended);

        // Update attendee-level attendance
        updateAttendeeAttendance(attendeeID, sessionID, attended);
    }
}