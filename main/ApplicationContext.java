package main;

import controller.*;
import model.repository.*;
import model.service.*;

public class ApplicationContext {
    private static final ApplicationContext INSTANCE = new ApplicationContext();

    // Repositories
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final FeedbackRepository feedbackRepository;
    private final CertificateRepository certificateRepository;
    private final ConferenceRepository conferenceRepository;

    // Services
    private final AttendeeService attendeeService;
    private final AuthService authService;
    private final CertificateService certificateService;
    private final ConferenceService conferenceService;
    private final FeedbackService feedbackService;
    private final NotificationService notificationService;
    private final SessionService sessionService;
    private final SpeakerService speakerService;

    // Controllers
    private final ManagerController managerController;
    private final AttendeeController attendeeController;
    private final SpeakerController speakerController;

    // Private constructor to initialize all components
    private ApplicationContext() {
        // Initialize repositories
        userRepository = new UserRepository();
        sessionRepository = new SessionRepository();
        feedbackRepository = new FeedbackRepository();
        certificateRepository = new CertificateRepository();
        conferenceRepository = new ConferenceRepository();

        // Initialize services
        sessionService = new SessionService(sessionRepository, userRepository);
        speakerService = new SpeakerService(userRepository, sessionRepository);
        feedbackService = new FeedbackService(feedbackRepository);
        attendeeService = new AttendeeService(userRepository, sessionService, feedbackService);
        certificateService = new CertificateService(certificateRepository, attendeeService);
        conferenceService = new ConferenceService(conferenceRepository);
        notificationService = new NotificationService();
        authService = new AuthService(userRepository);

        // Initialize controllers
        managerController = new ManagerController(
                speakerService, sessionService, feedbackService, attendeeService, certificateService, authService);
        attendeeController = new AttendeeController(
                sessionService, speakerService, attendeeService, certificateService, authService);
        speakerController = new SpeakerController(speakerService, sessionService, authService);
    }

    // Public method to access the singleton instance
    public static ApplicationContext getInstance() {
        return INSTANCE;
    }

    // Getters for Repositories
    public UserRepository getUserRepository() {
        return userRepository;
    }

    public SessionRepository getSessionRepository() {
        return sessionRepository;
    }

    public FeedbackRepository getFeedbackRepository() {
        return feedbackRepository;
    }

    public CertificateRepository getCertificateRepository() {
        return certificateRepository;
    }

    public ConferenceRepository getConferenceRepository() {
        return conferenceRepository;
    }

    // Getters for Services
    public AttendeeService getAttendeeService() {
        return attendeeService;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public CertificateService getCertificateService() {
        return certificateService;
    }

    public ConferenceService getConferenceService() {
        return conferenceService;
    }

    public FeedbackService getFeedbackService() {
        return feedbackService;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public SessionService getSessionService() {
        return sessionService;
    }

    public SpeakerService getSpeakerService() {
        return speakerService;
    }

    // Getters for Controllers
    public ManagerController getManagerController() {
        return managerController;
    }

    public AttendeeController getAttendeeController() {
        return attendeeController;
    }

    public SpeakerController getSpeakerController() {
        return speakerController;
    }
}