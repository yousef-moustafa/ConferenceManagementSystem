package model.domain;

import model.domain.enums.SessionStatus;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.time.*;
import java.util.stream.Collectors;

public class Session {
    private String sessionID;
    private String sessionName;
    private String speakerID;
    private Date date;
    private LocalTime time;
    private String room;
    private List<String> attendeeIDs;
    private int capacity;
    private SessionStatus status;
    private Map<String, Boolean> attendeeAttendance = new HashMap<>();


    public Session() {
        this.sessionID = generateSessionID();
        this.attendeeIDs = new ArrayList<>();
    }

    // Generate Unique ID
    private String generateSessionID() {
        return UUID.randomUUID().toString();
    }

    // Getters
    public String getSessionID() {
        return sessionID;
    }

    public String getSessionName() {
        return sessionName;
    }

    public String getSpeakerID() {
        return speakerID;
    }

    public Date getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getRoom() {
        return room;
    }

    public int getCapacity() {
        return capacity;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public int getAttendeeCount() {
        return attendeeIDs.size();
    }

    public List<String> getAttendeeIDs() {
        return attendeeIDs;
    }

    public Map<String, Boolean> getAttendeeAttendance() {
        return attendeeAttendance;
    }

    // Setters
    public void setSessionID(String sessionID) { this.sessionID = sessionID; }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public void setSpeakerID(String speakerID) {
        this.speakerID = speakerID;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public void setAttendeeAttendance(Map<String, Boolean> attendeeAttendance) {
        this.attendeeAttendance = attendeeAttendance;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String attendance = attendeeAttendance.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(";"));

        return String.join(", ",
                sessionID,
                sessionName,
                speakerID,
                dateFormat.format(date),
                time.toString(),
                room,
                String.valueOf(capacity),
                status.toString(),
                String.join(" ", attendeeIDs),
                attendance
        );
    }

}
