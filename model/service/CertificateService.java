package model.service;

import model.domain.Certificate;
import model.domain.PersonalizedSchedule;
import model.dto.AttendeeDTO;
import model.dto.CertificateDTO;
import model.repository.CertificateRepository;
import model.repository.UserRepository;
import model.dto.DTOMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final UserRepository userRepository;
    private final AttendeeService attendeeService;

    public CertificateService(CertificateRepository certificateRepository, UserRepository userRepository, AttendeeService attendeeService) {
        this.certificateRepository = certificateRepository;
        this.userRepository = userRepository;
        this.attendeeService = attendeeService;
    }

    // Generate a certificate for a single attendee
    public String generateCertificate(String attendeeID, String conferenceName) {
        Certificate certificate = new Certificate(attendeeID, conferenceName, new Date());
        certificateRepository.save(certificate);
        return certificate.getCertificateID();
    }

    // Retrieve a certificate by ID
    public CertificateDTO getCertificate(String certificateID) {
        Certificate certificate = certificateRepository.findById(certificateID);
        if (certificate == null) {
            throw new IllegalArgumentException("Certificate with ID " + certificateID + " not found.");
        }
        return DTOMapper.mapCertificateToDTO(certificate);
    }

    // Generate certificates for eligible attendees
    public List<String> generateCertificatesForEligibleAttendees(List<String> attendeeIDs, String conferenceName) {
        List<String> generatedCertificates = new ArrayList<>();
        for (String attendeeID : attendeeIDs) {
            if (isEligibleForCertificate(attendeeID)) {
                generatedCertificates.add(generateCertificate(attendeeID, conferenceName));
            }
        }
        return generatedCertificates;
    }

    // Check eligibility for a certificate
    private boolean isEligibleForCertificate(String attendeeID) {
        // Retrieve the attendee's profile using AttendeeService
        AttendeeDTO attendee = attendeeService.getAttendeeProfile(attendeeID);
        if (attendee == null) {
            return false;
        }

        // Fetch the attendee's personalized schedule
        PersonalizedSchedule schedule = attendeeService.getAttendeeSchedule(attendeeID);
        if (schedule == null) {
            return false;
        }

        // Check if all registered sessions are attended
        List<String> attendedSessions = attendee.getAttendedSessions();
        List<String> registeredSessions = schedule.getSessionsIDs();
        return attendedSessions.containsAll(registeredSessions);
    }

}
