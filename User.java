import java.util.*;

public class User {
    private String userID;
    private String userName;
    private String email;
    private Date registrationDate;

    public User(String userID, String userName, String email, Date registrationDate) {
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.registrationDate = registrationDate;
    }
}
