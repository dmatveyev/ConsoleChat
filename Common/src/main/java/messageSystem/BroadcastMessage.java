package messageSystem;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Денис on 16.03.2018.
 */
public class BroadcastMessage extends Message {
    private final String text;
    private final String userName;
    private final LocalDate date;
    private final LocalTime time;

    public BroadcastMessage(final String text, final String userName) {
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
    public String toString() {
        return date + "T" + time
                + " " + userName + ": " + text;

    }
}
