package messageSystem;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Денис on 16.03.2018.
 */
public class BroadcastMessage extends Message {
    private String text;
    private String userName;
    private LocalDate date;
    private LocalTime time;

    public BroadcastMessage(String text, String userName) {
        this.date = LocalDate.now();
        this.time = LocalTime.now();
        this.text = text;
        this.userName = userName;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public LocalTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return date.toString() +"T"+ time
                + " "+ userName +": " + text ;

    }
}
