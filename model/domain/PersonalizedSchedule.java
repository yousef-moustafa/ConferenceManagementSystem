package model.domain;

import java.util.*;

public class PersonalizedSchedule {
    private String scheduleID;
    private String attendeeID;
    private List<String> sessionIDs;

    public PersonalizedSchedule(String attendeeID, List<String> sessionsIDs) {
        this.scheduleID = generateUniqueID();
        this.attendeeID = attendeeID;
        this.sessionIDs = sessionsIDs;
    }

    // ID Generator
    private String generateUniqueID() {
        return UUID.randomUUID().toString();
    }

    // Getters
    public String getScheduleID() {
        return scheduleID;
    }

    public String getAttendeeID() {
        return attendeeID;
    }

    public List<String> getSessionsIDs() {
        return sessionIDs;
    }

    // Methods
    public boolean addSession(String sessionID) {
        // Check if session already in schedule
        if (!sessionIDs.contains(sessionID)) {
            sessionIDs.add(sessionID);
            return true;
        }
        return false;
    }

    public boolean removeSession(String sessionID) {
        return sessionIDs.remove(sessionID);
    }

    public boolean validateSchedule() {
        // Simple validation logic
        return sessionIDs != null && !sessionIDs.isEmpty();
    }

    // Setter for scheduleID
    public void setScheduleID(String scheduleID) {
        this.scheduleID = scheduleID;
    }

    @Override
    public String toString() {
        return scheduleID + ", " + attendeeID + ", " + String.join(" ", sessionIDs);
    }
}
