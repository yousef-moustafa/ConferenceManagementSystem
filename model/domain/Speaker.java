package model.domain;

import model.domain.enums.UserRole;

import java.time.LocalDate;
import java.util.*;

public class Speaker extends User {
    private String bio;
    private List<String> associatedSessionIDs;

    public Speaker(String userID, String userName, String email, LocalDate registrationDate, String bio, List<String> associatedSessionIDs) {
        super(userID, userName, email, registrationDate, UserRole.SPEAKER);
        this.bio = bio;
        this.associatedSessionIDs = associatedSessionIDs;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + bio + ", " + String.join(" ", associatedSessionIDs);
    }
}
