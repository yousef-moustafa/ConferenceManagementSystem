package controller;

import model.domain.FeedbackReport;
import model.domain.enums.SessionStatus;
import model.dto.AttendeeDTO;
import model.dto.FeedbackDTO;
import model.dto.SessionDTO;
import model.dto.SpeakerDTO;
import model.service.*;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManagerController {
    private final SpeakerService speakerService;
    private final SessionService sessionService;
    private final FeedbackService feedbackService;
    private final AttendeeService attendeeService;
    private final CertificateService certificateService;
    private final AuthService authService;

    public ManagerController(
            SpeakerService speakerService,
            SessionService sessionService,
            FeedbackService feedbackService,
            AttendeeService attendeeService,
            CertificateService certificateService,
            AuthService authService
    ) {
        this.speakerService = speakerService;
        this.sessionService = sessionService;
        this.feedbackService = feedbackService;
        this.attendeeService = attendeeService;
        this.certificateService = certificateService;
        this.authService = authService;
    }

    // ----- Speaker Methods -----
    public void loadSpeakers(DefaultTableModel speakerTableModel) {
        speakerTableModel.setRowCount(0);
        List<SpeakerDTO> speakers = speakerService.getAllSpeakers();
        for (SpeakerDTO speaker : speakers) {
            speakerTableModel.addRow(new Object[]{
                    speaker.getSpeakerID(),
                    speaker.getName(),
                    speaker.getEmail(),
                    speaker.getBio()
            });
        }
    }

    public String addSpeaker(SpeakerDTO newSpeaker, String password, DefaultTableModel speakerTableModel) throws Exception {
        String speakerID = speakerService.createSpeaker(newSpeaker, password);
        loadSpeakers(speakerTableModel);
        return speakerID;
    }

    public SpeakerDTO getSpeakerProfile(String speakerID) {
        return speakerService.getSpeakerProfile(speakerID);
    }

    public void updateSpeakerProfile(String speakerID, String name, String bio, DefaultTableModel speakerTableModel) {
        speakerService.updateSpeakerProfile(speakerID, name, bio);
        loadSpeakers(speakerTableModel);
    }

    public void deleteSpeaker(String speakerID, DefaultTableModel speakerTableModel) {
        speakerService.deleteSpeaker(speakerID);
        loadSpeakers(speakerTableModel);
    }

    // ----- Session Methods -----
    public void loadSessions(DefaultTableModel sessionTableModel) {
        sessionTableModel.setRowCount(0);
        List<SessionDTO> sessions = sessionService.getAllSessions();
        for (SessionDTO session : sessions) {
            String speakerName = "";
            if (session.getSpeakerID() != null) {
                SpeakerDTO speaker = speakerService.getSpeakerProfile(session.getSpeakerID());
                if (speaker != null) {
                    speakerName = speaker.getName();
                }
            }
            sessionTableModel.addRow(new Object[]{
                    session.getSessionID(),
                    session.getSessionName(),
                    speakerName,
                    session.getDate(),
                    session.getTime(),
                    session.getRoom(),
                    session.getCapacity(),
                    session.getStatus().toString()
            });
        }
    }

    public String addSession(SessionDTO newSession, String speakerName, DefaultTableModel sessionTableModel) throws Exception {
        String sessionID = sessionService.createSession(newSession);
        newSession.setSessionID(sessionID);
        loadSessions(sessionTableModel);
        return sessionID;
    }

    public SessionDTO getSession(String sessionID) {
        return sessionService.getSession(sessionID);
    }

    public void updateSession(SessionDTO session, DefaultTableModel sessionTableModel) {
        sessionService.updateSession(session);
        loadSessions(sessionTableModel);
    }

    public void deleteSession(String sessionID, DefaultTableModel sessionTableModel) {
        sessionService.deleteSession(sessionID);
        loadSessions(sessionTableModel);
    }

    public List<SpeakerDTO> getAllSpeakers() {
        return speakerService.getAllSpeakers();
    }

    public void loadSessionsIntoComboBox(javax.swing.JComboBox<String> sessionComboBox) {
        sessionComboBox.removeAllItems();
        List<SessionDTO> sessions = sessionService.getAllSessions();
        for (SessionDTO session : sessions) {
            sessionComboBox.addItem(session.getSessionID() + ": " + session.getSessionName());
        }
    }

    // ----- Attendee Methods -----
    public void loadAttendees(DefaultTableModel attendeeTableModel) {
        attendeeTableModel.setRowCount(0);
        List<AttendeeDTO> attendees = attendeeService.getAllAttendees();
        for (AttendeeDTO attendee : attendees) {
            String attendanceSummary = attendee.getAttendedSessions().size() + "/" +
                    attendeeService.getAttendeeSchedule(attendee.getAttendeeID()).getSessionsIDs().size();
            attendeeTableModel.addRow(new Object[]{
                    attendee.getAttendeeID(),
                    attendee.getName(),
                    attendee.getEmail(),
                    attendanceSummary
            });
        }
    }

    public void searchAttendees(DefaultTableModel attendeeTableModel, String query) {
        attendeeTableModel.setRowCount(0);
        List<AttendeeDTO> filteredAttendees = attendeeService.searchAttendees(query);
        for (AttendeeDTO attendee : filteredAttendees) {
            String attendanceSummary = attendee.getAttendedSessions().size() + "/" +
                    attendeeService.getAttendeeSchedule(attendee.getAttendeeID()).getSessionsIDs().size();
            attendeeTableModel.addRow(new Object[]{
                    attendee.getAttendeeID(),
                    attendee.getName(),
                    attendee.getEmail(),
                    attendanceSummary
            });
        }
    }

    public void loadAttendeesForSession(DefaultTableModel sessionAttendeesTableModel, String sessionID) {
        sessionAttendeesTableModel.setRowCount(0);

        List<AttendeeDTO> attendees = sessionService.getSessionAttendees(sessionID);
        Map<String, Boolean> attendanceMap = sessionService.getSession(sessionID).getAttendeeAttendance();
        for (AttendeeDTO attendee : attendees) {
            Boolean attended = attendanceMap.getOrDefault(attendee.getAttendeeID(), false);
            sessionAttendeesTableModel.addRow(new Object[]{
                    attendee.getAttendeeID(),
                    attendee.getName(),
                    attendee.getEmail(),
                    attended
            });
        }
    }

    public void markAttendanceForSelectedSession(String sessionID, List<String> attendeeIDs, Map<String, Boolean> attendanceMap,
                                                 DefaultTableModel sessionAttendeesTableModel) {
        // Update attendance for each attendee
        for (String attendeeID : attendeeIDs) {
            boolean attended = attendanceMap.get(attendeeID);
            attendeeService.markAttendance(attendeeID, sessionID, attended);
        }

        // Reload attendees for session
        loadAttendeesForSession(sessionAttendeesTableModel, sessionID);
    }

    // ----- Feedback Methods -----
    public void loadFeedbackTable(DefaultTableModel feedbackTableModel) {
        feedbackTableModel.setRowCount(0);
        List<FeedbackDTO> feedbackDTOs = feedbackService.getAllFeedback();
        for (FeedbackDTO feedback : feedbackDTOs) {
            feedbackTableModel.addRow(new Object[]{
                    feedback.getFeedbackID(),
                    feedback.getAttendeeID(),
                    feedback.getType(),
                    feedback.getDetails()
            });
        }
    }

    public FeedbackReport getConferenceFeedbackAnalysis() {
        return feedbackService.getConferenceFeedbackAnalysis();
    }

    public void exportFeedbackReport(String outputPath) throws IOException {
        feedbackService.exportFeedbackReport(outputPath);
    }

    // ----- Certificate Methods -----
    public List<String> issueCertificatesForDisplayedAttendees(List<String> attendeeIDs) throws Exception {
        return certificateService.generateCertificatesForEligibleAttendees(attendeeIDs, "GAF-AI 2025");
    }

    // ----- Auth / Logout -----
    public void logout() {
        authService.logout();
    }
}
