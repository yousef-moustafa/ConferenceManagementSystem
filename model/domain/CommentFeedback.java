package model.domain;

public class CommentFeedback extends Feedback {
    private final String comment;

    public CommentFeedback(String feedbackID, String attendeeID, String comment) {
        super(feedbackID, attendeeID);
        this.comment = comment;
    }

    // Getter and setter
    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "COMMENT, " + super.toString() + ", " + comment;
    }
}
