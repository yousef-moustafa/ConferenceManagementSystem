package model.domain;

import java.util.*;

public class FeedbackReport {
    private double averageRating;
    private int totalResponses;
    private List<String> commentsSummary;

    public FeedbackReport(double averageRating, int totalResponses, List<String> commentsSummary) {
        this.averageRating = averageRating;
        this.totalResponses = totalResponses;
        this.commentsSummary = commentsSummary;
    }

    // Getters
    public double getAverageRating() {
        return averageRating;
    }

    public int getTotalResponses() {
        return totalResponses;
    }

    public List<String> getCommentsSummary() {
        return commentsSummary;
    }
}
