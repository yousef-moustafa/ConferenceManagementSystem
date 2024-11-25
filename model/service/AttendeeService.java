package model.service;

import model.domain.Attendee;
import model.domain.PersonalizedSchedule;
import model.dto.AttendeeDTO;
import model.dto.DTOMapper;
import model.repository.UserRepository;
import model.service.SessionService;

import java.util.ArrayList;
import java.util.List;

public class AttendeeService {
    private final UserRepository userRepository;
    private final SessionService sessionService; // To handle cross-references between attendees and sessions
    private final List<PersonalizedSchedule> schedules; // Temporary in-memory storage for schedules

    public AttendeeService(SessionService sessionService) {
        this.userRepository = new UserRepository();
        this.sessionService = sessionService;
        this.schedules = new ArrayList<>();
    }

    // Create a new attendee
    public String createAttendee(AttendeeDTO attendeeDTO) {
        Attendee attendee = DTOMapper.mapDTOToAttendee(attendeeDTO);

        // Create a personalized schedule for the attendee
        String scheduleID = "SCHEDULE-" + (schedules.size() + 1);
        PersonalizedSchedule schedule = new PersonalizedSchedule(scheduleID, attendee.getUserID(), new ArrayList<>());
        schedules.add(schedule);

        attendee.setPersonalizedScheduleID(scheduleID);
        userRepository.save(attendee);

        return attendee.getUserID(); // Return the new attendee's ID
    }

    // Update an existing attendee's profile
    public void updateAttendeeProfile(String attendeeID, AttendeeDTO attendeeDTO) {
        Attendee attendee = (Attendee) userRepository.findById(attendeeID);
        if (attendee != null) {
            attendee.setUserName(attendeeDTO.getName());
            attendee.setEmail(attendeeDTO.getEmail());
            userRepository.save(attendee);
        }
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

    // Update an attendee's personalized schedule
    public boolean updateAttendeePersonalizedSchedule(String attendeeID, List<String> sessionIDs) {
        PersonalizedSchedule schedule = getAttendeeSchedule(attendeeID);
        if (schedule != null) {
            schedule.getSessionsIDs().clear();
            schedule.getSessionsIDs().addAll(sessionIDs);
            return schedule.validateSchedule(); // Return true if the schedule is valid
        }
        return false; // Schedule not found
    }

    // Register an attendee for a session
    public boolean registerAttendeeForSession(String attendeeID, String sessionID) {
        PersonalizedSchedule schedule = getAttendeeSchedule(attendeeID);
        if (schedule != null) {
            if (schedule.addSession(sessionID)) {
                sessionService.addAttendeeToSession(sessionID, attendeeID); // Ensure session reflects the change
                return true; // Registration successful
            }
        }
        return false; // Registration failed
    }
}