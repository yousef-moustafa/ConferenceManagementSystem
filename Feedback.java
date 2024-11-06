import java.time.*;

public class Feedback {
    private String feedbackID;
    private String attendeeID;
    private LocalDateTime timestamp;

    public Feedback() {
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
}
