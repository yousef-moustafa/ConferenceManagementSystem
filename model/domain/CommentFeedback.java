package model.domain;

public class CommentFeedback extends Feedback {
    private String comment;

    public CommentFeedback(String attendeeID, String comment) {
        super(attendeeID);
        if (comment == null || comment.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment cannot be empty.");
        }
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "COMMENT, " + super.toString() + ", " + comment;
    }
}

