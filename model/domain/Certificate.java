package model.domain;

import model.domain.enums.CertificateStatus;

import java.util.*;

public class Certificate {
    private String certificateID;
    private String attendeeID;
    private String conferenceName;
    private Date issueDate;
    private CertificateStatus status;

    public Certificate() {
    }

    // Getters
    public String getCertificateID() {
        return certificateID;
    }

    public String getAttendeeID() {
        return attendeeID;
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
        return "model.domain.Certificate for " + attendeeID + " from " + conferenceName + "\nIssued on: " + issueDate.toString();
    }
}
