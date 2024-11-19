package model.domain;

import java.time.*;

public class Feedback {
    private String feedbackID;
    private String attendeeID;
    private LocalDateTime timestamp;

    public Feedback() {
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public String getFeedbackID() {
        return feedbackID;
    }

    public String getAttendeeID() {
        return attendeeID;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setFeedbackID(String feedbackID) {
        this.feedbackID = feedbackID;
    }

    public void setAttendeeID(String attendeeID) {
        this.attendeeID = attendeeID;
    }
}
