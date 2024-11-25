package model.service;

import model.domain.Conference;
import model.dto.ConferenceDTO;
import model.repository.ConferenceRepository;
import model.dto.DTOMapper;

public class ConferenceService {
    private final ConferenceRepository conferenceRepository;

    public ConferenceService() {
        this.conferenceRepository = new ConferenceRepository();
    }

    // Initialize a new conference
    public void initializeConference(ConferenceDTO conferenceDTO) {
        // Use DTOMapper to convert ConferenceDTO to Conference
        Conference conference = DTOMapper.mapDTOToConference(conferenceDTO);
        conferenceRepository.save(conference);
    }

    // Update the details of an existing conference
    public void updateConferenceDetails(ConferenceDTO conferenceDTO) {
        Conference conference = conferenceRepository.findAll().stream().findFirst().orElse(null);
        if (conference != null) {
            conference.setConferenceName(conferenceDTO.getConferenceName());
            conference.setStartDate(conferenceDTO.getStartDate());
            conference.setEndDate(conferenceDTO.getEndDate());
            conferenceRepository.save(conference);
        }
    }


    // Retrieve conference details
    public ConferenceDTO getConferenceDetails() {
        // Find the conference
        Conference conference = conferenceRepository.findAll().stream().findFirst().orElse(null);
        if (conference != null) {
            return DTOMapper.mapConferenceToDTO(conference);
        }
        return null;
    }


    // Delete the current conference
    public void deleteConference() {
        Conference conference = conferenceRepository.findAll().stream().findFirst().orElse(null);
        if (conference != null) {
            // Delete by conferenceID
            conferenceRepository.delete(conference.getConferenceID());
        }
    }
    
}
