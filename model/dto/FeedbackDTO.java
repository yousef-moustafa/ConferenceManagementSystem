package model.dto;

public class FeedbackDTO {
    private String feedbackID;
    private String attendeeID;
    private String type;
    private String details;

    // Constructor
    public FeedbackDTO(String feedbackID, String attendeeID, String type, String details) {
        this.feedbackID = feedbackID;
        this.attendeeID = attendeeID;
        this.type = type;
        this.details = details;
    }

    // Getters and setters
    public String getFeedbackID() { return feedbackID; }
    public String getAttendeeID() { return attendeeID; }
    public String getType() { return type; }
    public String getDetails() { return details; }
}
