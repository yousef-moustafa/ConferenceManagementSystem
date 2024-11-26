package model.domain;

import model.domain.enums.UserRole;

import java.time.LocalDate;
import java.util.*;

public class Attendee extends User {
    private List<String> registeredSessionIDs;
    private String personalizedScheduleID;
    private String certificateID;
    private String feedbackID;

    public Attendee(String userID, String userName, String email, String password, LocalDate registrationDate, List<String> registeredSessionIDs, String personalizedScheduleID, String certificateID, String feedbackID) {
        super(userID, userName, email, password, registrationDate, UserRole.ATTENDEE);
        this.registeredSessionIDs = registeredSessionIDs;
        this.personalizedScheduleID = personalizedScheduleID;
        this.certificateID = certificateID;
        this.feedbackID = feedbackID;
    }

    public List<String> getRegisteredSessionIDs() {
        return registeredSessionIDs;
    }

    public String getPersonalizedScheduleID() {
        return personalizedScheduleID;
    }

    public void setPersonalizedScheduleID(String personalizedScheduleID) { // Add this setter
        this.personalizedScheduleID = personalizedScheduleID;
    }

    public String getCertificateID() {
        return certificateID;
    }

    public String getFeedbackID() {
        return feedbackID;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + String.join(" ", registeredSessionIDs) + ", " + personalizedScheduleID + ", " + certificateID + ", " + feedbackID;
    }
}

