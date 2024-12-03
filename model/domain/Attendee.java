package model.domain;

import model.domain.enums.UserRole;

import java.time.LocalDate;
import java.util.*;

public class Attendee extends User {
    private String personalizedScheduleID;
    private String certificateID;
    private String ratingFeedbackID;
    private String commentFeedbackID;

    public Attendee(String userName, String email, String password, LocalDate registrationDate, String personalizedScheduleID, String certificateID, String ratingFeedbackID, String commentFeedbackID) {
        super(userName, email, password, registrationDate, UserRole.ATTENDEE);
        this.personalizedScheduleID = personalizedScheduleID;
        this.certificateID = certificateID;
        this.ratingFeedbackID = ratingFeedbackID;
        this.commentFeedbackID = commentFeedbackID;
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

    public String getRatingFeedbackID() {
        return ratingFeedbackID;
    }

    public String getCommentFeedbackID() {
        return commentFeedbackID;
    }

    public void setRatingFeedbackID(String ratingFeedbackID) {
        this.ratingFeedbackID = ratingFeedbackID;
    }

    public void setCommentFeedbackID(String commentFeedbackID) {
        this.commentFeedbackID = commentFeedbackID;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + personalizedScheduleID + ", " + certificateID + ", " + ratingFeedbackID + ", " + commentFeedbackID;
    }
}

