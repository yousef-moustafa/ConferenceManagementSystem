package model.service;

import model.domain.Session;
import model.domain.Attendee;
import model.dto.SessionDTO;
import model.dto.AttendeeDTO;
import model.repository.SessionRepository;
import model.repository.UserRepository;
import model.dto.DTOMapper;
import model.domain.enums.SessionStatus;

import java.util.ArrayList;
import java.util.List;

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
            sessionRepository.save(session);
        }
    }

    // Remove an attendee from a session
    public void removeAttendeeFromSession(String sessionId, String attendeeId) {
        Session session = sessionRepository.findById(sessionId);
        if (session != null) {
            session.getAttendeeIDs().remove(attendeeId);
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


}