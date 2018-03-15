package client;

import client.message.Message;
import client.message.User;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class UserMessageReader implements ClientSubject {
    private Message userMessage;
    private ArrayList<ClientObserver> observers;
    private Scanner in;

    public UserMessageReader () {
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
        User user = new User(String.valueOf(Math.random()),"default", "default");
        while (in.hasNextLine()){
            userMessage = new Message(in.nextLine(), user.getUserId(),
                    LocalDate.now(), LocalTime.now());
            userMessage.setMessageType("broadcast");
            notifyObservers();
        }

    }
}