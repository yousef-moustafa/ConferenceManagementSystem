package model.service;

import model.domain.Speaker;
import model.domain.Session;
import model.domain.User;
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

    public SpeakerService(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    // Create a new speaker
    public String createSpeaker(SpeakerDTO speakerDTO, String password) {
        Speaker speaker = DTOMapper.mapDTOToSpeaker(speakerDTO, password);
        userRepository.save(speaker);
        return speaker.getUserID(); // Return the generated ID for the speaker
    }

    // Update a speaker's name and bio
    public void updateSpeakerProfile(String speakerID, String name, String bio) {
        Speaker speaker = (Speaker) userRepository.findById(speakerID);
        if (speaker != null) {
            if (speaker.getUserName() != null) {
                speaker.setUserName(name);
            }
            if (speaker.getBio() != null) {
                speaker.setBio(bio);
            }
            userRepository.save(speaker);
        }
    }

    // Update a speaker's bio
    public void updateSpeakerBio(String speakerID, String bio) {
        Speaker speaker = (Speaker) userRepository.findById(speakerID);
        if (speaker != null) {
            speaker.setBio(bio);
            userRepository.save(speaker);
        }
    }

    // Delete a speaker by  ID
    public void deleteSpeaker(String speakerID) {
        Speaker speaker = (Speaker) userRepository.findById(speakerID);
        if (speaker != null) {
            userRepository.delete(speakerID);
        } else {
            throw new IllegalArgumentException("Speaker with ID " + speakerID + " not found.");
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

    // Retrieve all speakers as DTOs along with their IDs
    public List<SpeakerDTO> getAllSpeakers() {
        List<User> users = userRepository.findAll();
        List<SpeakerDTO> speakers = new ArrayList<>();

        for (User user : users) {
            if (user instanceof Speaker) {
                Speaker speaker = (Speaker) user;
                speakers.add(DTOMapper.mapSpeakerToDTO(speaker));
            }
        }

        return speakers;
    }
}