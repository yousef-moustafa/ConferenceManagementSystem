import java.util.*;

public class PersonalizedSchedule {
    private String scheduleID;
    private String attendeeID;
    private List<String> sessionIDs;

    public PersonalizedSchedule(String scheduleID, String attendeeID, List<String> sessionsIDs) {
        this.scheduleID = scheduleID;
        this.attendeeID = attendeeID;
        this.sessionIDs = sessionsIDs;
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
}
