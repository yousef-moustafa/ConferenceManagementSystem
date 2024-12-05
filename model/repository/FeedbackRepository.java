package model.repository;

import model.domain.Feedback;
import model.domain.RatingFeedback;
import model.domain.CommentFeedback;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.time.format.DateTimeFormatter;

public class FeedbackRepository implements Repository<Feedback> {
    private List<Feedback> feedbackList = new ArrayList<>();
    private File file = new File("feedback.csv");

    public FeedbackRepository() {
        loadFromFile();
    }

    @Override
    public void save(Feedback entity) {
        // Remove existing feedback with the same ID
        feedbackList.removeIf(feedback -> feedback.getFeedbackID().equals(entity.getFeedbackID()));

        feedbackList.add(entity);
        writeToFile(); // Save feedback to file after adding
    }

    @Override
    public Feedback findById(String id) {
        for (Feedback feedback : feedbackList) {
            if (feedback.getFeedbackID().equals(id)) {
                return feedback;
            }
        }
        return null; // Return null if not found
    }

    @Override
    public List<Feedback> findAll() {
        return feedbackList;
    }

    @Override
    public void delete(String id) {
        feedbackList.removeIf(feedback -> feedback.getFeedbackID().equals(id));
        writeToFile(); // Write changes to file after deletion
    }

    public Feedback findRatingFeedbackByAttendeeID(String attendeeID) {
        return feedbackList.stream()
                .filter(f -> f instanceof RatingFeedback && f.getAttendeeID().equals(attendeeID))
                .findFirst()
                .orElse(null);
    }

    public Feedback findCommentFeedbackByAttendeeID(String attendeeID) {
        return feedbackList.stream()
                .filter(f -> f instanceof CommentFeedback && f.getAttendeeID().equals(attendeeID))
                .findFirst()
                .orElse(null);
    }

    public void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                String type = data[0]; // First field is the type (e.g., "COMMENT" or "RATING")
                String attendeeID = data[2];

                if ("COMMENT".equals(type)) {
                    String comment = data[4];
                    feedbackList.add(new CommentFeedback(attendeeID, comment));
                } else if ("RATING".equals(type)) {
                    int rating = Integer.parseInt(data[4]);
                    feedbackList.add(new RatingFeedback(attendeeID, rating));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Feedback feedback : feedbackList) {
                writer.write(feedback.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}