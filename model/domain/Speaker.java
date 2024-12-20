package model.domain;

import model.domain.enums.UserRole;

import java.time.LocalDate;
import java.util.*;

public class Speaker extends User {
    private String bio;
    private List<String> associatedSessionIDs;

    public Speaker(String userName, String email, String password, LocalDate registrationDate, String bio, List<String> associatedSessionIDs) {
        super(userName, email, password, registrationDate, UserRole.SPEAKER);
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
        String sessionIDs = "";
        if (!associatedSessionIDs.isEmpty()) {
            sessionIDs = String.join(" ", associatedSessionIDs);
        }
        return super.toString() + ", " + bio + ", " + sessionIDs;
    }

    public List<String> getAssociatedSessionIDs() {
        return associatedSessionIDs;
    }
}
