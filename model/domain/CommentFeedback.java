package model.domain;

public class CommentFeedback extends Feedback {
    private String comment;

    public CommentFeedback(String comment) {
        this.comment = comment;
    }

    public CommentFeedback(String feedbackID, String attendeeID, String comment) {
        setFeedbackID(feedbackID);
        setAttendeeID(attendeeID);
        this.comment = comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
