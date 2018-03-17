package messageSystem;



import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Денис on 16.03.2018.
 */
public class AuthMessage extends Message {
    private String userid;
    private String userlogin;
    private String userPassword;
    private LocalDate date;
    private LocalTime time;

    public AuthMessage (String userId, String userlogin, String userPassword) {
        this.date = LocalDate.now();
        this.time = LocalTime.now();
        this.userid = userId;
        this.userlogin = userlogin;
        this.userPassword = userPassword;
    }

    public String getUserid() {
        return userid;
    }

    public String getUserlogin() {
        return userlogin;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
