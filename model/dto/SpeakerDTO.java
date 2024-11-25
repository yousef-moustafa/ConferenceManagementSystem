package model.dto;
import java.util.List;

public class SpeakerDTO {
    private String name;
    private String email;
    private String bio;
    private List<String> associatedSessionIDs; // Optional field

    public SpeakerDTO() {}

    public SpeakerDTO(String name, String email, String bio, List<String> associatedSessionIDs) {
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.associatedSessionIDs = associatedSessionIDs;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public List<String> getAssociatedSessionIDs() {
        return associatedSessionIDs;
    }

    public void setAssociatedSessionIDs(List<String> associatedSessionIDs) {
        this.associatedSessionIDs = associatedSessionIDs;
    }

    @Override
    public String toString() {
        return "SpeakerDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", bio='" + bio + '\'' +
                ", associatedSessionIDs=" + associatedSessionIDs +
                '}';
    }
}
