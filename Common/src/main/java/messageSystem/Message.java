package messageSystem;

import java.io.Serializable;

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
