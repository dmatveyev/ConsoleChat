package messageSystem;


import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Денис on 16.03.2018.
 */
public class AuthMessage extends Message {
    private final String userId;
    private final String userLogin;
    private final String userPassword;
    private final LocalDate date;
    private final LocalTime time;

    public AuthMessage(final String userId, final String userLogin, final String userPassword) {
        this.date = LocalDate.now();
        this.time = LocalTime.now();
        this.userId = userId;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    String getUserPassword() {
        return userPassword;
    }

    @Override
    public String toString() {
        return "AuthMessage{" +
                "userLogin='" + userLogin + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
