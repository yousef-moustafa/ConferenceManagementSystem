package model.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Feedback {
    private final String feedbackID;
    private final String attendeeID;
    private final LocalDateTime timestamp;

    public Feedback(String feedbackID, String attendeeID) {
        this.feedbackID = feedbackID;
        this.attendeeID = attendeeID;
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

    @Override
    public String toString() {
        return feedbackID + ", " + attendeeID + ", " + timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
