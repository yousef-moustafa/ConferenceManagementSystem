package model.repository;

import model.domain.Certificate;
import model.domain.enums.CertificateStatus;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class CertificateRepository implements Repository<Certificate> {
    private List<Certificate> certificates = new ArrayList<>();
    private File file = new File("certificates.csv");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public CertificateRepository() {
        loadFromFile();
    }

    @Override
    public void save(Certificate entity) {
        certificates.add(entity);
        writeToFile();
    }

    @Override
    public Certificate findById(String id) {
        for (Certificate certificate : certificates) {
            if (certificate.getCertificateID().equals(id)) {
                return certificate;
            }
        }
        return null;
    }

    @Override
    public List<Certificate> findAll() {
        return certificates;
    }

    @Override
    public void delete(String id) {
        certificates.removeIf(certificate -> certificate.getCertificateID().equals(id));
        writeToFile();
    }

    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (data.length == 4) {
                    Certificate certificate = new Certificate(
                            data[0],
                            data[1],
                            dateFormat.parse(data[2])
                    );
                    certificate.setStatus(CertificateStatus.valueOf(data[3].toUpperCase()));
                    certificates.add(certificate);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading certificates from file", e);
        }
    }
    private void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Certificate certificate : certificates) {
                writer.write(String.format("%s, %s, %s, %s, %s",
                        certificate.getCertificateID(),
                        certificate.getAttendeeID(),
                        certificate.getConferenceName(),
                        dateFormat.format(certificate.getIssueDate()),
                        certificate.getStatus()));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing certificates to file", e);
        }
    }
}
