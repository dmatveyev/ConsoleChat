package messageSystem;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Денис on 06.03.2018.
 */
public class Message implements Serializable {
    private String text;
    private String userName;
    private LocalDate date;
    private LocalTime time;
    private String messageType;

    public LocalTime getTime() {
        return time;
    }
    public Message () {

    }

    public Message (String text, String userName, LocalDate date, LocalTime time) {
        this.text = text;
        this.userName = userName;
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
                + " "+ userName +": " + getText() ;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
