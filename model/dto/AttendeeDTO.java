package model.dto;

// AttendeeDTO - Basic attendee information
public class AttendeeDTO {
    private String name;
    private String email;

    public AttendeeDTO() {}

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
