package model.service;

import model.domain.Attendee;
import model.domain.PersonalizedSchedule;
import model.domain.User;
import model.domain.enums.AuthResult;
import model.repository.UserRepository;

public class AuthService {

    private final UserRepository userRepository;
    private User currentUser;

    // Constructor
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.currentUser = null;
    }

    // Login method
    public AuthResult login(String email, String password) {
        if (email == null || password == null) {
            return AuthResult.SYSTEM_ERROR;
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return AuthResult.USER_NOT_FOUND;
        }
        if (user.verifyPassword(password)) {
            this.currentUser = user;
            System.out.println("Login successful. Welcome, " + user.getUserName() + "!");
            return AuthResult.SUCCESS;
        } else {
            return AuthResult.INVALID_CREDENTIALS;
        }
    }

    // Logout method
    public void logout() {
        if (currentUser != null) {
            System.out.println("Goodbye, " + currentUser.getUserName() + "!");
            this.currentUser = null;
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    // Verify attendee method
    public AuthResult verifyAttendee(String attendeeID) {
        if (attendeeID == null) {
            return AuthResult.SYSTEM_ERROR;
        }
        User user = userRepository.findById(attendeeID);
        if (user == null) {
            return AuthResult.USER_NOT_FOUND;
        }
        if (user instanceof Attendee) {
            return AuthResult.SUCCESS;
        } else {
            return AuthResult.NOT_REGISTERED;
        }
    }


    // Get the currently logged-in user
    public User getCurrentUser() {
        return currentUser;
    }
}
