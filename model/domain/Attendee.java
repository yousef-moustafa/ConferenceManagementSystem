package model.domain;

import model.domain.enums.UserRole;

import java.time.LocalDate;
import java.util.*;

public class Attendee extends User {
    private String personalizedScheduleID;
    private String certificateID;
    private String feedbackID;

    public Attendee(String userName, String email, String password, LocalDate registrationDate, String personalizedScheduleID, String certificateID, String feedbackID) {
        super(userName, email, password, registrationDate, UserRole.ATTENDEE);
        this.personalizedScheduleID = personalizedScheduleID;
        this.certificateID = certificateID;
        this.feedbackID = feedbackID;
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
        return super.toString() + ", " + personalizedScheduleID + ", " + certificateID + ", " + feedbackID;
    }
}

