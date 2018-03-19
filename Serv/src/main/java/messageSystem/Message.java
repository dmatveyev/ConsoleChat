package messageSystem;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Денис on 06.03.2018.
 */
public class Message implements Serializable {
    private String text;

    public Message() {
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
