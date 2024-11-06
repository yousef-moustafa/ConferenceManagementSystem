public class RatingFeedback extends Feedback{
    private int rating;
    private int maxRating;

    public RatingFeedback(int rating) {
        this.rating = rating;
        this.maxRating = 5;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
