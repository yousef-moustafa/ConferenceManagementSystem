package model.dto;
import java.util.ArrayList;
import java.util.List;

public class AttendeeDTO {
    private String attendeeID;
    private String name;
    private String email;
    private String personalizedScheduleID; // For viewing or managing registered sessions
    private List<String> attendedSessions = new ArrayList<>();

    public AttendeeDTO() {}

    // Constructor without ID
    public AttendeeDTO(String attendeeID, String name, String email, String personalizedScheduleID, List<String> attendedSessions) {
        this.attendeeID = attendeeID;
        this.name = name;
        this.email = email;
        this.personalizedScheduleID = personalizedScheduleID;
        this.attendedSessions = attendedSessions;
    }

    // Getters and setters
    public String getAttendeeID() { return attendeeID; }
    public void setAttendeeID(String attendeeID) {
        this.attendeeID = attendeeID;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPersonalizedScheduleID() { return personalizedScheduleID; }
    public void setPersonalizedScheduleID(String personalizedScheduleID) { this.personalizedScheduleID = personalizedScheduleID; }

    public List<String> getAttendedSessions() {
        return attendedSessions;
    }
    public void setAttendedSessions(List<String> attendedSessions) {
        this.attendedSessions = attendedSessions;
    }

    @Override
    public String toString() {
        return "AttendeeDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", personalizedScheduleID='" + personalizedScheduleID + '\'' +
                ", attendedSessions=" + attendedSessions +
                '}';
    }
}
