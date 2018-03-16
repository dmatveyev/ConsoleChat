package client;

import messageSystem.Message;
import messageSystem.User;

import java.util.ArrayList;

public class MessageManager implements Observer, Subject, ClientObserver {
    private final User user;
    private Message message;

    private ArrayList<Observer> observers;

    public MessageManager(User user) {
        this.user = user;
        observers = new ArrayList<>();
    }


    public void doMessage() {
        String msgType = message.getMessageType();
        if (msgType.equals("broadcast"))
            display();
        if (msgType.equals("auth")){
            user.setUserId(message.getText());
            System.out.printf("Hello, %s!!!\n", user.getLogin());
        }
    }
    @Override
    public void update(Message message) {
        this.message = message;
        doMessage();
    }
    public void display() {
        System.out.println(message);
    }

    @Override
    public void registerObserver(final Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObservers(final Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer: observers) {
            observer.update(message);
        }
    }

    @Override
    public void updateClient(final Message userMessage) {

            for (Observer observer: observers) {
                observer.update(userMessage);
            }

    }


}