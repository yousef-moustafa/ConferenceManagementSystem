package model.dto;
import java.util.List;

public class AttendeeDTO {
    private String name;
    private String email;
    private List<String> registeredSessionIDs; // For viewing or managing registered sessions

    public AttendeeDTO() {}

    public AttendeeDTO(String name, String email, List<String> registeredSessionIDs) {
        this.name = name;
        this.email = email;
        this.registeredSessionIDs = registeredSessionIDs;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<String> getRegisteredSessionIDs() { return registeredSessionIDs; }
    public void setRegisteredSessionIDs(List<String> registeredSessionIDs) { this.registeredSessionIDs = registeredSessionIDs; }

    @Override
    public String toString() {
        return "AttendeeDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", registeredSessionIDs=" + registeredSessionIDs +
                '}';
    }
}
