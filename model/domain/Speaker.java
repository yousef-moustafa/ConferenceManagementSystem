package model.domain;

import model.domain.enums.UserRole;

import java.util.*;

public class Speaker extends User {
    private String bio;
    private List<String> associatedSessionIDs;

    public Speaker(String userID, String userName, String email, Date registrationDate, UserRole userRole, String bio, List<String> associatedSessionIDs) {
        super(userID, userName, email, registrationDate, userRole);
        this.bio = bio;
        this.associatedSessionIDs = associatedSessionIDs;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
