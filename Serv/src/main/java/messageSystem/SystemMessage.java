package messageSystem;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Денис on 16.03.2018.
 */
public class SystemMessage extends Message {
    public SystemMessage(String text, String userName, LocalDate date, LocalTime time) {
        super(text, userName, date, time);
    }
}
