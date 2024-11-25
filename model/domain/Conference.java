package model.domain;

import java.util.*;

public class Conference {
    private String conferenceID;
    private String conferenceName;
    private Date startDate;
    private Date endDate;
    private List<String> attendeeIDs;
    private List<String> sessionIDs;

    public Conference() {
    }

    // Getters
    public String getConferenceID() {
        return conferenceID;
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public List<String> getAttendeeIDs() { return attendeeIDs; }

    public List<String> getSessionIDs() { return sessionIDs; }

    // Setters
    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
