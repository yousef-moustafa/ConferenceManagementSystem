package model.domain;

import model.domain.enums.UserRole;

import java.util.*;

public class Attendee extends User {
    private List<String> registeredSessionIDs;
    private String personalizedScheduleID;
    private String certificateID;
    private String feedbackID;

    public Attendee(String userID, String userName, String email, Date registrationDate, UserRole userRole, List<String> registeredSessionIDs, String personalizedScheduleID, String certificateID, String feedbackID) {
        super(userID, userName, email, registrationDate, userRole);
        this.registeredSessionIDs = registeredSessionIDs;
        this.personalizedScheduleID = personalizedScheduleID;
        this.certificateID = certificateID;
        this.feedbackID = feedbackID;
    }
}

