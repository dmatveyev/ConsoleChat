package server.messagePool;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Денис on 06.03.2018.
 */
public class Message {
    private String text;
    private String login;
    private LocalDate date;
    private LocalTime time;

    public LocalTime getTime() {
        return time;
    }

    public Message (String text, String login, LocalDate date, LocalTime time) {
        this.text = text;
        this.login = login;
        this.date = date;
        this.time = time;

    }
    public String getText() {
        return  text;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getLogin(){
        return  login;
    }

    @Override
    public String toString() {
        return getDate().toString() +"T"+ getTime()
                + " "+ getLogin()+": " + getText() ;
    }
}
