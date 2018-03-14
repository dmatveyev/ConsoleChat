package client.message;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Денис on 14.03.2018.
 */
public class UserMessage implements Serializable {
    private String text;
    User user;
    private LocalDate date;
    private LocalTime time;

    public LocalTime getTime() {
        return time;
    }

    public UserMessage(String text, User user, LocalDate date, LocalTime time) {
        this.text = text;
        this.user = user;
        this.date = date;
        this.time = time;

    }
    public String getText() {
        return  text;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return getDate().toString() +"T"+ getTime()
                + " "+ user.getLogin()+": " + getText() ;
    }
}
