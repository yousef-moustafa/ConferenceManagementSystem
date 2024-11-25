package model.service;
import model.domain.Conference;
import model.dto.ConferenceDTO;
import model.repository.ConferenceRepository;

public class ConferenceService {
    private final ConferenceRepository conferenceRepository;

    public ConferenceService() {
        this.conferenceRepository = new ConferenceRepository();
    }

    // Initialize a new conference
    public void initializeConference(ConferenceDTO conferenceDTO) {
        Conference conference = new Conference();
        conference.setConferenceName(conferenceDTO.getConferenceName());
        conference.setStartDate(conferenceDTO.getStartDate());
        conference.setEndDate(conferenceDTO.getEndDate());

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


    public ConferenceDTO getConferenceDetails() {
        Conference conference = conferenceRepository.findAll().stream().findFirst().orElse(null);
        if (conference != null) {
            return new ConferenceDTO(
                    conference.getConferenceName(),
                    conference.getStartDate(),
                    conference.getEndDate(),
                    conference.getSessionIDs(),
                    conference.getAttendeeIDs()
            );
        }
        return null;
    }


    // Delete the current conference
    public void deleteConference() {
        Conference conference = conferenceRepository.findAll().stream().findFirst().orElse(null);
        if (conference != null) {
            conferenceRepository.delete(conference.getConferenceID());
        }
    }
    
}
