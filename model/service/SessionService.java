package model.service;

import model.domain.Session;
import model.domain.Attendee;
import model.domain.Speaker;
import model.dto.SessionDTO;
import model.dto.AttendeeDTO;
import model.repository.SessionRepository;
import model.repository.UserRepository;
import model.dto.DTOMapper;
import model.domain.enums.SessionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SessionService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public SessionService() {
        this.sessionRepository = new SessionRepository();
        this.userRepository = new UserRepository();
    }

    // Create a new session
    public String createSession(SessionDTO sessionDTO) {
        Session session = DTOMapper.mapDTOToSession(sessionDTO);
        sessionRepository.save(session);

        // Assign session to speaker if a speakerID is provided
        if (sessionDTO.getSpeakerID() != null) {
            assignSessionToSpeaker(sessionDTO.getSpeakerID(), session.getSessionID());
        }

        return session.getSessionID();
    }

    // Update an existing session
    public void updateSession(SessionDTO sessionDTO) {
        Session existingSession = sessionRepository.findById(sessionDTO.getSessionID());
        if (existingSession != null) {
            existingSession.setSessionName(sessionDTO.getSessionName());
            existingSession.setSpeakerID(sessionDTO.getSpeakerID());
            existingSession.setDate(sessionDTO.getDate());
            existingSession.setTime(sessionDTO.getTime());
            existingSession.setRoom(sessionDTO.getRoom());
            existingSession.setCapacity(sessionDTO.getCapacity());
            existingSession.setStatus(sessionDTO.getStatus());

            // Check if the speakerID has changed
            if (!sessionDTO.getSpeakerID().equals(existingSession.getSpeakerID())) {
                assignSessionToSpeaker(sessionDTO.getSpeakerID(), sessionDTO.getSessionID());
            }

            sessionRepository.save(existingSession);
        }
    }

    // Delete a session by ID
    public void deleteSession(String sessionID) {
        sessionRepository.delete(sessionID);
    }

    // Retrieve session by ID
    public SessionDTO getSession(String sessionID) {
        Session session = sessionRepository.findById(sessionID);
        if (session != null) {
            return DTOMapper.mapSessionToDTO(session);
        }
        return null;
    }

    // Retrieve all session
    public List<SessionDTO> getAllSessions() {
        List<Session> sessions = sessionRepository.findAll();
        List<SessionDTO> sessionDTOs = new ArrayList<>();
        for (Session session : sessions) {
            sessionDTOs.add(DTOMapper.mapSessionToDTO(session));
        }
        return sessionDTOs;
    }

    // Add an attendee to a session
    public void addAttendeeToSession(String sessionId, String attendeeId) {
        Session session = sessionRepository.findById(sessionId);
        if (session != null && session.getAttendeeCount() < session.getCapacity()) {
            session.getAttendeeIDs().add(attendeeId);

            session.getAttendeeAttendance().put(attendeeId, false); // Default to "not present"
            sessionRepository.save(session);
        }
    }

    // Remove an attendee from a session
    public void removeAttendeeFromSession(String sessionId, String attendeeId) {
        Session session = sessionRepository.findById(sessionId);
        if (session != null) {
            session.getAttendeeIDs().remove(attendeeId);
            session.getAttendeeAttendance().remove(attendeeId); // Remove attendance entry
            sessionRepository.save(session);
        }
    }

    public List<AttendeeDTO> getSessionAttendees(String sessionId) {
        Session session = sessionRepository.findById(sessionId);
        if (session != null) {
            List<AttendeeDTO> attendeeDTOs = new ArrayList<>();

            // Loop through each attendee ID in the session
            for (String attendeeId : session.getAttendeeIDs()) {
                Attendee attendee = (Attendee) userRepository.findById(attendeeId);
                if (attendee != null) {
                    attendeeDTOs.add(DTOMapper.mapAttendeeToDTO(attendee));
                }
            }

            return attendeeDTOs;
        }
        return new ArrayList<>();
    }

    // Update session status
    public void updateSessionStatus(String sessionId, SessionStatus status) {
        Session session = sessionRepository.findById(sessionId);
        if (session != null) {
            session.setStatus(status);
            sessionRepository.save(session);
        }
    }

    public void notifySessionCancellation(String sessionId) {
        Session session = sessionRepository.findById(sessionId);
        // if (session != null) {
        // for (String attendeeId : session.getAttendeeIDs()) {
        // Commented out until NotificationService is implemented
        // sendNotification(attendeeId, "Session " + session.getSessionName() + " has been cancelled.");
        // }
        //}
    }

    // Assign a session to a speaker
    public void assignSessionToSpeaker(String speakerID, String sessionID) {
        userRepository.loadFromFile();

        Speaker speaker = (Speaker) userRepository.findById(speakerID);
        Session session = sessionRepository.findById(sessionID);

        if (speaker != null && session != null) {
            // Add sessionID to speaker's associated sessions if not already present
            if (!speaker.getAssociatedSessionIDs().contains(sessionID)) {
                speaker.getAssociatedSessionIDs().add(sessionID);
                userRepository.save(speaker);
            }

            // Assign speakerID to the session
            session.setSpeakerID(speakerID);
            sessionRepository.save(session);
        }
    }

    // Method for marking attendance for a single attendee
    public void markAttendance(String sessionID, String attendeeID, boolean attended) {
        Session session = sessionRepository.findById(sessionID);
        if (session == null) {
            throw new IllegalArgumentException("Session not found with ID: " + sessionID);
        }

        // Update or add the specific attendee's attendance in the existing map
        session.getAttendeeAttendance().put(attendeeID, attended);

        // Persist the updated session
        sessionRepository.save(session);
    }

    // Method to handle bulk attendance updates (if needed)
    public void markAttendance(String sessionID, Map<String, Boolean> attendanceMap) {
        for (Map.Entry<String, Boolean> entry : attendanceMap.entrySet()) {
            markAttendance(sessionID, entry.getKey(), entry.getValue());
        }
    }



}