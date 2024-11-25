package model.service;

import model.domain.Speaker;
import model.domain.Session;
import model.dto.SpeakerDTO;
import model.dto.SessionDTO;
import model.dto.DTOMapper;
import model.repository.UserRepository;
import model.repository.SessionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SpeakerService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public SpeakerService() {
        this.userRepository = new UserRepository();
        this.sessionRepository = new SessionRepository();
    }

    // Create a new speaker
    public String createSpeaker(SpeakerDTO speakerDTO) {
        Speaker speaker = DTOMapper.mapDTOToSpeaker(speakerDTO);
        userRepository.save(speaker);
        return speaker.getUserID(); // Return the generated ID for the speaker
    }

    // Update a speaker's bio
    public void updateSpeakerBio(String speakerID, String bio) {
        Speaker speaker = (Speaker) userRepository.findById(speakerID);
        if (speaker != null) {
            speaker.setBio(bio);
            userRepository.save(speaker);
        }
    }

    // Get a speaker's profile
    public SpeakerDTO getSpeakerProfile(String speakerID) {
        Speaker speaker = (Speaker) userRepository.findById(speakerID);
        if (speaker != null) {
            return DTOMapper.mapSpeakerToDTO(speaker);
        }
        return null;
    }

    // Get all sessions assigned to a speaker
    public List<SessionDTO> getSpeakerSessions(String speakerID) {
        Speaker speaker = (Speaker) userRepository.findById(speakerID);
        if (speaker != null) {
            List<String> sessionIDs = speaker.getAssociatedSessionIDs();
            return sessionIDs.stream()
                    .map(sessionRepository::findById)
                    .filter(Objects::nonNull)
                    .map(DTOMapper::mapSessionToDTO)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    // Assign a session to a speaker
    public void assignSessionToSpeaker(String speakerID, String sessionID) {
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
}