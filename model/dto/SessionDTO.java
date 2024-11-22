package model.dto;
import java.util.Date;
import java.time.LocalTime;

public class SessionDTO {
    private String sessionName;
    private String speakerName;
    private Date date;
    private LocalTime time;
    private String room;
    private int capacity;

    public SessionDTO() {}

    // Getters and setters
    public String getSessionName() { return sessionName; }
    public void setSessionName(String sessionName) { this.sessionName = sessionName; }

    public String getSpeakerName() { return speakerName; }
    public void setSpeakerName(String speakerName) { this.speakerName = speakerName; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
}

