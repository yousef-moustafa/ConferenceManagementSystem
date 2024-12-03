package model.dto;
import java.util.List;

public class AttendeeDTO {
    private String attendeeID;
    private String name;
    private String email;
    private String personalizedScheduleID; // For viewing or managing registered sessions

    public AttendeeDTO() {}

    // Constructor without ID
    public AttendeeDTO(String name, String email, String personalizedScheduleID) {
        this.name = name;
        this.email = email;
        this.personalizedScheduleID = personalizedScheduleID;
    }

    // Getters and setters
    public String getAttendeeID() { return attendeeID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPersonalizedScheduleID() { return personalizedScheduleID; }
    public void setPersonalizedScheduleID(String personalizedScheduleID) { this.personalizedScheduleID = personalizedScheduleID; }

    @Override
    public String toString() {
        return "AttendeeDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", PersonalizedSchedule=" + personalizedScheduleID +
                '}';
    }
}
