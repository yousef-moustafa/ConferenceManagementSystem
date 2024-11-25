package model.dto;
import java.util.Date;

public class ConferenceDTO {
    private String conferenceName;
    private Date startDate;
    private Date endDate;

    public ConferenceDTO() {}

    public ConferenceDTO(String conferenceName, Date startDate, Date endDate) {
        this.conferenceName = conferenceName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and setters
    public String getConferenceName() { return conferenceName; }
    public void setConferenceName(String conferenceName) { this.conferenceName = conferenceName; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    @Override
    public String toString() {
        return "ConferenceDTO{" +
                "conferenceName='" + conferenceName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
