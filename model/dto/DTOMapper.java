package model.dto;

import model.domain.Session;
import model.domain.Attendee;
import model.domain.Speaker;
import model.domain.Certificate;
import model.domain.Feedback;
import model.domain.CommentFeedback;
import model.domain.RatingFeedback;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


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
                session.getStatus(),
                new HashMap<>(session.getAttendeeAttendance())
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
        session.setAttendeeAttendance(dto.getAttendeeAttendance());
        return session;
    }


    // Attendee mappings
    public static AttendeeDTO mapAttendeeToDTO(Attendee attendee) {
        if (attendee == null) {
            return null;
        }

        return new AttendeeDTO(
                attendee.getUserID(),
                attendee.getUserName(),
                attendee.getEmail(),
                attendee.getPersonalizedScheduleID(),
                new ArrayList<>(attendee.getAttendedSessions())
        );
    }

    public static Attendee mapDTOToAttendee(AttendeeDTO dto, String password) {
        if (dto == null) {
            return null;
        }

        return new Attendee(
                dto.getName(),
                dto.getEmail(),
                password,
                LocalDate.now(),
                dto.getPersonalizedScheduleID(),
                null,
                null,
                null,
                new HashSet<>(dto.getAttendedSessions()) // Convert List to Set
        );
    }

    // Speaker mappings
    public static SpeakerDTO mapSpeakerToDTO(Speaker speaker) {
        if (speaker == null) {
            return null;
        }

        return new SpeakerDTO(
                speaker.getUserID(),
                speaker.getUserName(),
                speaker.getEmail(),
                speaker.getBio(),
                speaker.getAssociatedSessionIDs()
        );
    }

    public static Speaker mapDTOToSpeaker(SpeakerDTO dto, String password) {
        if (dto == null) {
            return null;
        }

        Speaker speaker = new Speaker(
                dto.getName(),
                dto.getEmail(),
                password,
                LocalDate.now(),
                dto.getBio(),
                dto.getAssociatedSessionIDs()
        );
        return speaker;
    }

    // Certificate mapping
    public static CertificateDTO mapCertificateToDTO(Certificate certificate) {
        if (certificate == null) {
            return null;
        }

        return new CertificateDTO(
                certificate.getCertificateID(),
                certificate.getAttendeeID(),
                certificate.getConferenceName(),
                certificate.getIssueDate()
        );
    }

    // Feedback mappings
    public static FeedbackDTO mapFeedbackToDTO(Feedback feedback) {
        if (feedback instanceof RatingFeedback ratingFeedback) {
            return new FeedbackDTO(
                    ratingFeedback.getFeedbackID(),
                    ratingFeedback.getAttendeeID(),
                    "Rating",
                    ratingFeedback.getRating() + " / 5"
            );
        } else if (feedback instanceof CommentFeedback commentFeedback) {
            return new FeedbackDTO(
                    commentFeedback.getFeedbackID(),
                    commentFeedback.getAttendeeID(),
                    "Comment",
                    commentFeedback.getComment()
            );
        }
        throw new IllegalArgumentException("Unknown feedback type.");
    }
}

