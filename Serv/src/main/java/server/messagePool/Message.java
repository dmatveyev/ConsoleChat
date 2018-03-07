package server.messagePool;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Денис on 06.03.2018.
 */
public class Message {
    private String text;
    private int userId;
    private LocalDate date;
    private LocalTime time;

    public LocalTime getTime() {
        return time;
    }

    public Message (String text, int userId, LocalDate date, LocalTime time) {
        this.text = text;
        this.userId = userId;
        this.date = date;
        this.time = time;

    }
    public String getText() {
        return  text;
    }
    public int getUserId() {
        return userId;
    }
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return getDate().toString() +"T"+ getTime() + " "+ getUserId()+": " + getText() ;
    }
}
