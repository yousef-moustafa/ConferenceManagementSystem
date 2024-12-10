package model.service;

import model.domain.Feedback;
import model.domain.RatingFeedback;
import model.domain.CommentFeedback;
import model.dto.DTOMapper;
import model.dto.FeedbackDTO;
import model.repository.FeedbackRepository;
import model.domain.FeedbackReport;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
        // Check for existing rating feedback
        Feedback existingFeedback = feedbackRepository.findRatingFeedbackByAttendeeID(attendeeID);
        if (existingFeedback != null) {
            // Update the existing rating feedback
            ((RatingFeedback) existingFeedback).setRating(rating);
            feedbackRepository.save(existingFeedback); // Save the updated feedback
            return existingFeedback.getFeedbackID();
        }

        // Create new feedback if none exists
        Feedback newFeedback = new RatingFeedback(attendeeID, rating);
        feedbackRepository.save(newFeedback);
        return newFeedback.getFeedbackID();
    }

    // Submit a comment feedback
    public String submitComment(String attendeeID, String comment) {
        // Check for existing comment feedback
        Feedback existingFeedback = feedbackRepository.findCommentFeedbackByAttendeeID(attendeeID);
        if (existingFeedback != null) {
            // Update the existing comment feedback
            ((CommentFeedback) existingFeedback).setComment(comment);
            feedbackRepository.save(existingFeedback); // Save the updated feedback
            return existingFeedback.getFeedbackID();
        }

        // Create new feedback if none exists
        Feedback newFeedback = new CommentFeedback(attendeeID, comment);
        feedbackRepository.save(newFeedback);
        return newFeedback.getFeedbackID();
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

    public void exportFeedbackReport(String outputPath) throws IOException {
        // Generate feedback analysis
        FeedbackReport report = getConferenceFeedbackAnalysis();

        // Prepare the report content
        StringBuilder reportContent = new StringBuilder();
        reportContent.append("Conference Feedback Report\n");
        reportContent.append("==========================\n\n");
        reportContent.append("Total Responses: ").append(report.getTotalResponses()).append("\n");
        reportContent.append("Average Rating: ").append(String.format("%.2f", report.getAverageRating())).append("\n\n");

        reportContent.append("Comments Summary:\n");
        for (String comment : report.getCommentsSummary()) {
            reportContent.append("- ").append(comment).append("\n");
        }

        // Write to the output file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write(reportContent.toString());
        }
    }

}
