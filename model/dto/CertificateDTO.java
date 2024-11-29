package model.dto;
import java.util.Date;

public class CertificateDTO {
    private String certificateID;
    private String attendeeID;       // Use attendeeID instead of attendeeName
    private String conferenceName;
    private Date issueDate;

    public CertificateDTO() {}

    public CertificateDTO(String certificateID, String attendeeID, String conferenceName, Date issueDate) {
        this.certificateID = certificateID;
        this.attendeeID = attendeeID;
        this.conferenceName = conferenceName;
        this.issueDate = issueDate;
    }

    // Getters and setters
    public String getCertificateID() { return certificateID; }

    public String getAttendeeID() { return attendeeID; }
    public void setAttendeeID(String attendeeID) { this.attendeeID = attendeeID; }

    public String getConferenceName() { return conferenceName; }
    public void setConferenceName(String conferenceName) { this.conferenceName = conferenceName; }

    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }

    @Override
    public String toString() {
        return "CertificateDTO{" +
                "attendeeID='" + attendeeID + '\'' +
                ", conferenceName='" + conferenceName + '\'' +
                ", issueDate=" + issueDate +
                '}';
    }
}
