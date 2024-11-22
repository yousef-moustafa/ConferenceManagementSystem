package model.dto;

// SpeakerDTO - Essential speaker information
public class SpeakerDTO {
    private String name;
    private String email;
    private String bio;

    public SpeakerDTO() {}

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}
