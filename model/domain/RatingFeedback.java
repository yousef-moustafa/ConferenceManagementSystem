package model.domain;

public class RatingFeedback extends Feedback {
    private int rating;
    private final int maxRating = 5; // Default maximum rating

    public RatingFeedback(String feedbackID, String attendeeID, int rating) {
        setFeedbackID(feedbackID);
        setAttendeeID(attendeeID);
        this.rating = rating;
    }

    // Getters and Setters for rating
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating >= 0 && rating <= maxRating) {
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("Rating must be between 0 and " + maxRating);
        }
    }

    public int getMaxRating() {
        return maxRating;
    }
}