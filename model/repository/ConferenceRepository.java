package model.repository;

import model.domain.Conference;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ConferenceRepository implements Repository<Conference> {
    private Conference conference; // Single conference instance
    private File file = new File("conference.csv");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ConferenceRepository() {
        loadFromFile();
    }

    @Override
    public void save(Conference entity) {
        this.conference = entity;
        writeToFile();
    }

    @Override
    public Conference findById(String id) {
        // Since there is only one conference, check if it matches
        if (conference != null && conference.getConferenceID().equals(id)) {
            return conference;
        }
        return null;
    }

    @Override
    public List<Conference> findAll() {
        // Return a list with a single conference or an empty list
        return conference != null ? List.of(conference) : List.of();
    }

    @Override
    public void delete(String id) {
        if (conference != null && conference.getConferenceID().equals(id)) {
            conference = null; // Delete the single conference
            writeToFile();
        }
    }

    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null) {
                String[] data = line.split(", ");
                if (data.length >= 3) {
                    Conference loadedConference = new Conference();
                    loadedConference.setConferenceName(data[0]);
                    loadedConference.setStartDate(dateFormat.parse(data[1]));
                    loadedConference.setEndDate(dateFormat.parse(data[2]));
                    conference = loadedConference;
                }
            }
        } catch (IOException | ParseException e) {
            // Handle missing or corrupted file gracefully
            conference = null; // No conference found
        }
    }

    private void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            if (conference != null) {
                writer.write(String.format("%s, %s, %s",
                        conference.getConferenceName(),
                        dateFormat.format(conference.getStartDate()),
                        dateFormat.format(conference.getEndDate())));
                writer.newLine();
            } else {
                // Clear file if conference is null
                writer.write("");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing conference to file", e);
        }
    }
}
