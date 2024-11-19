package model.domain;

public class RatingFeedback extends Feedback {
    private final int rating;
    private final int maxRating = 5; // Default maximum rating

    public RatingFeedback(String feedbackID, String attendeeID, int rating) {
        super(feedbackID, attendeeID);
        this.rating = rating;
    }

    // Getters and Setters for rating
    public int getRating() {
        return rating;
    }

    public int getMaxRating() {
        return maxRating;
    }

    @Override
    public String toString() {
        return "RATING, " + super.toString() + ", " + rating;
    }
}