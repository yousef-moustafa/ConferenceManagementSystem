package model.domain;

public class CommentFeedback extends Feedback {
    private String comment;

    public CommentFeedback(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
