package client.message;

import server.clientData.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Денис on 06.03.2018.
 */
public class Message implements Serializable {
    private String text;
    private String userId;
    private LocalDate date;
    private LocalTime time;

    public LocalTime getTime() {
        return time;
    }

    public Message (String text, String userId, LocalDate date, LocalTime time) {
        this.text = text;
        this.userId = userId;
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
                + " "+ userId+": " + getText() ;
    }
}