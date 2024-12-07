package model.service;

import model.domain.Certificate;
import model.domain.PersonalizedSchedule;
import model.dto.AttendeeDTO;
import model.dto.CertificateDTO;
import model.repository.CertificateRepository;
import model.dto.DTOMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final AttendeeService attendeeService;

    public CertificateService(AttendeeService attendeeService) {
        this.certificateRepository = new CertificateRepository();
        this.attendeeService = attendeeService;
    }

    // Generate a certificate for a single attendee
    public String generateCertificate(String attendeeID, String conferenceName) {
        // Check if a certificate already exists for the attendee and conference
        boolean certificateExists = certificateRepository.findAll().stream()
                .anyMatch(certificate -> certificate.getAttendeeID().equals(attendeeID)
                        && certificate.getConferenceName().equals(conferenceName));

        if (certificateExists) {
            throw new IllegalArgumentException("Certificate already exists for attendee: " + attendeeID);
        }

        // Create and save a new certificate
        Certificate certificate = new Certificate(attendeeID, conferenceName, new Date());
        certificateRepository.save(certificate);

        // Update the attendee's certificateID
        attendeeService.updateAttendeeCertificateID(attendeeID, certificate.getCertificateID());

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
        AttendeeDTO attendee = attendeeService.getAttendeeProfile(attendeeID);
        if (attendee == null) {
            return false;
        }
        PersonalizedSchedule schedule = attendeeService.getAttendeeSchedule(attendeeID);
        if (schedule == null) {
            return false;
        }

        List<String> registeredSessions = schedule.getSessionsIDs();
        List<String> attendedSessions = attendee.getAttendedSessions();

        // Check if registeredSessions is empty
        if (registeredSessions.isEmpty()) {
            System.out.println("Attendee " + attendeeID + " is not registered for any sessions.");
            return false; // Ineligible if attendee is not yet registered to any sessions
        }

        // Check if all registered sessions are attended
        boolean eligible = attendedSessions.containsAll(registeredSessions);
        System.out.println("Attendee " + attendeeID + " eligible: " + eligible);
        return eligible;
    }

    // Retrieve certificate associated with a specific attendee
    public CertificateDTO getCertificateForAttendee(String attendeeID) {
        for (Certificate certificate : certificateRepository.findAll()) {
            if (certificate.getAttendeeID().equals(attendeeID)) {
                return DTOMapper.mapCertificateToDTO(certificate); // Convert to DTO
            }
        }
        // Return null or throw an exception if no certificate is found
        throw new IllegalArgumentException("No certificate found for attendee with ID: " + attendeeID);
    }


}
