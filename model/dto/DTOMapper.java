package model.dto;

import model.domain.Session;
import model.domain.Attendee;
import model.domain.Speaker;
import model.domain.Conference;
import model.domain.Certificate;

import java.time.LocalDate;


public class DTOMapper {

    // Session mappings
    public static SessionDTO mapSessionToDTO(Session session) {
        if (session == null) {
            return null;
        }

        return new SessionDTO(
                session.getSessionID(),
                session.getSessionName(),
                session.getSpeakerID(),
                session.getDate(),
                session.getTime(),
                session.getRoom(),
                session.getCapacity(),
                session.getStatus()
        );
    }

    public static Session mapDTOToSession(SessionDTO dto) {
        if (dto == null) {
            return null;
        }

        Session session = new Session();
        session.setSessionName(dto.getSessionName());
        session.setSpeakerID(dto.getSpeakerID());
        session.setDate(dto.getDate());
        session.setTime(dto.getTime());
        session.setRoom(dto.getRoom());
        session.setCapacity(dto.getCapacity());
        session.setStatus(dto.getStatus());
        return session;
    }


    // Attendee mappings
    public static AttendeeDTO mapAttendeeToDTO(Attendee attendee) {
        if (attendee == null) {
            return null;
        }

        return new AttendeeDTO(
                attendee.getUserName(),
                attendee.getEmail(),
                attendee.getRegisteredSessionIDs()
        );
    }


    public static Attendee mapDTOToAttendee(AttendeeDTO dto, String password) { // Added password parameter
        if (dto == null) {
            return null;
        }

        return new Attendee(
                dto.getName(),
                dto.getEmail(),
                password,
                LocalDate.now(),
                dto.getRegisteredSessionIDs(),
                null,
                null,
                null
        );
    }

    // Speaker mappings
    public static SpeakerDTO mapSpeakerToDTO(Speaker speaker) {
        if (speaker == null) {
            return null;
        }

        return new SpeakerDTO(
                speaker.getUserName(),
                speaker.getEmail(),
                speaker.getBio(),
                speaker.getAssociatedSessionIDs()
        );
    }

    public static Speaker mapDTOToSpeaker(SpeakerDTO dto, String password) { // Added password parameter
        if (dto == null) {
            return null;
        }

        return new Speaker(
                dto.getName(),
                dto.getEmail(),
                password,
                LocalDate.now(),
                dto.getBio(),
                dto.getAssociatedSessionIDs()
        );
    }
    // Conference mappings
    public static ConferenceDTO mapConferenceToDTO(Conference conference) {
        if (conference == null) {
            return null;
        }

        return new ConferenceDTO(
                conference.getConferenceName(),
                conference.getStartDate(),
                conference.getEndDate()
        );
    }

    public static Conference mapDTOToConference(ConferenceDTO dto) {
        if (dto == null) {
            return null;
        }

        Conference conference = new Conference();
        conference.setConferenceName(dto.getConferenceName());
        conference.setStartDate(dto.getStartDate());
        conference.setEndDate(dto.getEndDate());
        return conference;
    }

    // Certificate mapping
    public static CertificateDTO mapCertificateToDTO(Certificate certificate) {
        if (certificate == null) {
            return null;
        }

        return new CertificateDTO(
                certificate.getAttendeeID(),
                certificate.getConferenceName(),
                certificate.getIssueDate()
        );
    }
}
