import java.util.*;

public class User {
    private String userID;
    private String userName;
    private String email;
    private Date registrationDate;
    private UserRole userRole;

    public User(String userID, String userName, String email, Date registrationDate, UserRole userRole) {
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.registrationDate = registrationDate;
        this.userRole = userRole;
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

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    // Setters needed
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
