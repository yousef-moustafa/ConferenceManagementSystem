package model.domain;

import model.domain.enums.CertificateStatus;

import java.util.*;

public class Certificate {
    private static int idCounter = 1;

    private String certificateID;
    private String attendeeID;
    private String conferenceName;
    private Date issueDate;
    private CertificateStatus status;

    public Certificate(String attendeeID, String conferenceName, Date issueDate) {
        this.certificateID = generateCertificateID();
        this.attendeeID = attendeeID;
        this.conferenceName = conferenceName;
        this.issueDate = issueDate;
        this.status = CertificateStatus.GENERATED; // Default status
    }

    // Generate Unique ID
    private String generateCertificateID() {
        return "CERT-" + (idCounter++);
    }

    // Getters
    public String getCertificateID() {
        return certificateID;
    }

    public String getAttendeeID() {
        return attendeeID;
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public CertificateStatus getStatus() {
        return status;
    }

    public void setStatus(CertificateStatus status) {
        this.status = status;
    }

    public String generateCertificateText() {
        return "Certificate for " + attendeeID + " from " + conferenceName + "\nIssued on: " + issueDate.toString();
    }
}
