package model.service;

import model.domain.Feedback;
import model.domain.RatingFeedback;
import model.domain.CommentFeedback;
import model.dto.DTOMapper;
import model.dto.FeedbackDTO;
import model.repository.FeedbackRepository;
import model.domain.FeedbackReport;

import java.util.ArrayList;
import java.util.List;

public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    // Constructor
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    // Submit a rating feedback
    public String submitRating(String attendeeID, int rating) {
        Feedback feedback = new RatingFeedback(attendeeID, rating);
        feedbackRepository.save(feedback);
        return feedback.getFeedbackID();
    }

    // Submit a comment feedback
    public String submitComment(String attendeeID, String comment) {
        Feedback feedback = new CommentFeedback(attendeeID, comment);
        feedbackRepository.save(feedback);
        return feedback.getFeedbackID();
    }

    // Update an existing feedback
    public void updateFeedback(String feedbackID, Feedback updatedFeedback) {
        Feedback existingFeedback = feedbackRepository.findById(feedbackID);
        if (existingFeedback == null) {
            throw new IllegalArgumentException("Feedback with ID " + feedbackID + " does not exist.");
        }
        feedbackRepository.delete(feedbackID);
        feedbackRepository.save(updatedFeedback);
    }

    // Retrieve a single feedback by its ID
    public Feedback getFeedback(String feedbackID) {
        Feedback feedback = feedbackRepository.findById(feedbackID);
        if (feedback == null) {
            throw new IllegalArgumentException("Feedback with ID " + feedbackID + " not found.");
        }
        return feedback;
    }

    // Retrieve all feedback
    public List<FeedbackDTO> getAllFeedback() {
        List<Feedback> feedbackList = feedbackRepository.findAll();
        List<FeedbackDTO> feedbackDTOs = new ArrayList<>();

        for (Feedback feedback : feedbackList) {
            FeedbackDTO feedbackDTO = DTOMapper.mapFeedbackToDTO(feedback);
            feedbackDTOs.add(feedbackDTO);
        }

        return feedbackDTOs;
    }


    // Retrieve all feedback for a specific attendee
    public List<FeedbackDTO> getAttendeeAllFeedback(String attendeeID) {
        return feedbackRepository.findAll().stream()
                .filter(feedback -> feedback.getAttendeeID().equals(attendeeID))
                .map(DTOMapper::mapFeedbackToDTO)
                .toList();
    }

    // Generate a feedback report for the conference
    public FeedbackReport getConferenceFeedbackAnalysis() {
        List<Feedback> feedbackList = feedbackRepository.findAll();

        int totalResponses = feedbackList.size();
        int totalRating = 0;
        int ratingCount = 0;
        List<String> commentsSummary = new ArrayList<>();

        for (Feedback feedback : feedbackList) {
            if (feedback instanceof RatingFeedback ratingFeedback) {
                totalRating += ratingFeedback.getRating();
                ratingCount++;
            } else if (feedback instanceof CommentFeedback commentFeedback) {
                commentsSummary.add(commentFeedback.getComment());
            }
        }

        double averageRating = ratingCount > 0 ? (double) totalRating / ratingCount : 0.0;

        return new FeedbackReport(averageRating, totalResponses, commentsSummary);
    }
}
