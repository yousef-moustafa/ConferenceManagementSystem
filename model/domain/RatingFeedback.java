package model.domain;

public class RatingFeedback extends Feedback {
    private final int rating;
    private final int maxRating = 5; // Default maximum rating

    public RatingFeedback(String attendeeID, int rating) {
        super(attendeeID);
        if (rating < 1 || rating > maxRating) {
            throw new IllegalArgumentException("Rating must be between 1 and " + maxRating + ".");
        }
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