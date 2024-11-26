package model.service;

import model.domain.Attendee;
import model.domain.Certificate;
import model.domain.User;
import model.domain.enums.CertificateStatus;
import model.dto.CertificateDTO;
import model.repository.CertificateRepository;
import model.repository.UserRepository;
import model.service.NotificationService;
import model.dto.DTOMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    // Modify the constructor to include UserRepository
    public CertificateService(CertificateRepository certificateRepository, NotificationService notificationService, UserRepository userRepository) {
        this.certificateRepository = certificateRepository;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
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

    // Generate certificates for all attendees
    public List<String> generateCertificatesForAllAttendees(List<String> attendeeIDs, String conferenceName) {
        List<String> generatedCertificates = new ArrayList<>();
        for (String attendeeID : attendeeIDs) {
            generatedCertificates.add(generateCertificate(attendeeID, conferenceName));
        }
        return generatedCertificates;
    }

    // Distribute certificates via email
    public void distributeCertificatesViaEmail() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Certificate certificate : certificateRepository.findAll()) {
            if (certificate.getStatus() == CertificateStatus.GENERATED) {
                String recipientEmail = getAttendeeEmailById(certificate.getAttendeeID());
                if (recipientEmail == null) {
                    System.out.println("Email not found for attendee ID: " + certificate.getAttendeeID());
                    continue;
                }
                String subject = "Certificate of Attendance for " + certificate.getConferenceName();
                String content = String.format(
                        "Dear %s,\n\n" +
                                "Congratulations! Your certificate for attending %s has been issued.\n" +
                                "Certificate ID: %s\n" +
                                "Issue Date: %s\n\n" +
                                "Thank you for attending!\n\n" +
                                "Regards,\nConference Team",
                        getAttendeeNameById(certificate.getAttendeeID()), // Get attendee name
                        certificate.getConferenceName(),
                        certificate.getCertificateID(),
                        dateFormat.format(certificate.getIssueDate())
                );

                notificationService.sendEmail(recipientEmail, subject, content);
                markCertificateAsSent(certificate.getCertificateID());
            }
        }
    }


    // Update certificate status
    public void updateCertificateStatus(String certificateID, CertificateStatus status) {
        Certificate certificate = certificateRepository.findById(certificateID);
        if (certificate == null) {
            throw new IllegalArgumentException("Certificate with ID " + certificateID + " not found.");
        }
        certificate.setStatus(status);
        certificateRepository.save(certificate);
    }

    // Mark a certificate as sent
    public void markCertificateAsSent(String certificateID) {
        updateCertificateStatus(certificateID, CertificateStatus.SENT);
    }

    // Get a certificate for a specific attendee
    public CertificateDTO getCertificateForAttendee(String attendeeID) {
        for (Certificate certificate : certificateRepository.findAll()) {
            if (certificate.getAttendeeID().equals(attendeeID)) {
                return DTOMapper.mapCertificateToDTO(certificate);
            }
        }
        throw new IllegalArgumentException("No certificate found for attendee with ID " + attendeeID);
    }

    // Validate a certificate by ID
    public boolean validateCertificate(String certificateID) {
        Certificate certificate = certificateRepository.findById(certificateID);
        if (certificate == null) {
            return false;
        }
        return certificate.getStatus() == CertificateStatus.SENT || certificate.getStatus() == CertificateStatus.VALIDATED;
    }

    // Helper method to get attendee's email by their ID
    private String getAttendeeEmailById(String attendeeID) {
        User user = userRepository.findById(attendeeID);
        if (user instanceof Attendee) {
            return user.getEmail();
        }
        return null;
    }

    private String getAttendeeNameById(String attendeeID) {
        User user = userRepository.findById(attendeeID);
        if (user instanceof Attendee) {
            return user.getUserName();
        }
        return "Attendee";
    }
}
