package model.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Feedback {
    private static int idCounter = 1; // Static counter for unique IDs

    private final String feedbackID;
    private final String attendeeID;
    private final LocalDateTime timestamp;

    // Constructor
    public Feedback(String attendeeID) {
        this.feedbackID = generateFeedbackID();
        this.attendeeID = attendeeID;
        this.timestamp = LocalDateTime.now();
    }

    // Generate Unique ID
    private String generateFeedbackID() {
        return "FEEDBACK-" + idCounter++;
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
