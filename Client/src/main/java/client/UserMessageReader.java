package client;

import client.message.Message;
import client.message.User;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class UserMessageReader implements ClientSubject {
    private final User user;
    private Message userMessage;
    private ArrayList<ClientObserver> observers;
    private Scanner in;

    public UserMessageReader (User user) {
        this.user = user;
        observers = new ArrayList<>();
         in = new Scanner(System.in);
    }

    @Override
    public void registerObserver(final ClientObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(final ClientObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (ClientObserver observer: observers) {
            observer.updateClient(userMessage);
        }
    }

    public void read() {
        System.out.println ("Enter Login");

        String login = "";
        String password = "";
        if (in.hasNextLine())
            login=in.nextLine();
        System.out.println ("Enter password");
        if (in.hasNextLine())
            password =in.nextLine();
        user.setLogin(login);
        user.setPassword(password);
        userMessage = new Message(login.concat(":").concat(password),login,
                LocalDate.now(), LocalTime.now());
        userMessage.setMessageType("auth");
        notifyObservers();
        //переключаемся на чтение обычных сообщений
        while (in.hasNextLine()){
            userMessage = new Message(in.nextLine(), user.getLogin(),
                    LocalDate.now(), LocalTime.now());
            userMessage.setMessageType("broadcast");
            notifyObservers();
        }
    }
}