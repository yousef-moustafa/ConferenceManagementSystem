package model.dto;
import java.util.Date;

// CertificateDTO - Basic certificate information
public class CertificateDTO {
    private String attendeeName;
    private String conferenceName;
    private Date issueDate;

    public CertificateDTO() {}

    // Getters and setters
    public String getAttendeeName() { return attendeeName; }
    public void setAttendeeName(String attendeeName) { this.attendeeName = attendeeName; }

    public String getConferenceName() { return conferenceName; }
    public void setConferenceName(String conferenceName) { this.conferenceName = conferenceName; }

    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }
}