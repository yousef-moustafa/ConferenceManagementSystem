package model.service;

public class NotificationService {

    // Sends an email notification
    public void sendEmail(String recipient, String subject, String content) {
        // Placeholder for email sending logic
        System.out.printf("Email sent to %s:\nSubject: %s\nContent:\n%s\n", recipient, subject, content);
    }

    // Notifies attendees of a session change (placeholder method)
    public void notifySessionChange(String sessionId) {
        System.out.printf("Notification sent for session change: %s\n", sessionId);
    }
}

