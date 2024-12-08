package model.domain;

import model.domain.enums.CertificateStatus;

import java.text.SimpleDateFormat;
import java.util.*;

public class Certificate {
    private static int idCounter = 1;

    private String certificateID;
    private String attendeeID;
    private String conferenceName;
    private Date issueDate;
    private CertificateStatus status;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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

    // Setters
    public void setStatus(CertificateStatus status) {
        this.status = status;
    }

    public void setCertificateID(String certificateID) {
        this.certificateID = certificateID;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s",
                certificateID,
                attendeeID,
                conferenceName,
                dateFormat.format(issueDate),
                status.name());
    }
}
