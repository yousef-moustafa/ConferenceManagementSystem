package model.dto;
import model.domain.enums.SessionStatus;

import java.util.Date;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class SessionDTO {
    private String sessionID;
    private String sessionName;
    private String speakerID;
    private Date date;
    private LocalTime time;
    private String room;
    private int capacity;
    private SessionStatus status;
    private Map<String, Boolean> attendeeAttendance = new HashMap<>();


    public SessionDTO() {}

    public SessionDTO(String sessionID, String sessionName, String speakerID, Date date, LocalTime time, String room, int capacity, SessionStatus status, Map<String, Boolean> attendeeAttendance) {
        this.sessionID = sessionID;
        this.sessionName = sessionName;
        this.speakerID = speakerID;
        this.date = date;
        this.time = time;
        this.room = room;
        this.capacity = capacity;
        this.status = status;
        this.attendeeAttendance = attendeeAttendance;
    }

    // Getters and setters
    public String getSessionID() { return sessionID; }
    public void setSessionID(String sessionID) { this.sessionID = sessionID; }

    public String getSessionName() { return sessionName; }
    public void setSessionName(String sessionName) { this.sessionName = sessionName; }

    public String getSpeakerID() { return speakerID; }
    public void setSpeakerID(String speakerID) { this.speakerID = speakerID; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public SessionStatus getStatus() { return status; }
    public void setStatus(SessionStatus status) { this.status = status; }

    public Map<String, Boolean> getAttendeeAttendance() {
        return attendeeAttendance;
    }
    public void setAttendeeAttendance(Map<String, Boolean> attendeeAttendance) {
        this.attendeeAttendance = attendeeAttendance;
    }

    @Override
    public String toString() {
        return "SessionDTO{" +
                "sessionID='" + sessionID + '\'' +
                ", sessionName='" + sessionName + '\'' +
                ", speakerID='" + speakerID + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", room='" + room + '\'' +
                ", capacity=" + capacity +
                ", status=" + status +
                '}';
    }
}

