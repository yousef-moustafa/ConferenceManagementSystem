package model.domain;

import model.domain.enums.UserRole;
import java.time.LocalDate;
import java.util.UUID;

public class User {
    private String userID;
    private String userName;
    private String email;
    private String password;
    private LocalDate registrationDate;
    private UserRole userRole;

    public User(String userName, String email, String password, LocalDate registrationDate, UserRole userRole) {
        this.userID = generateUniqueID();
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.userRole = userRole;
    }

    // ID Generator
    private String generateUniqueID() {
        return UUID.randomUUID().toString();
    }

    // Getters for all attributes
    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    // Setters needed
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) { this.password = password; }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password); // Replace with hashing logic later
    }


    @Override
    public String toString() {
        return userID + ", " + userName + ", " + email + ", " + password + ", " + registrationDate + ", " + userRole;
    }
}
