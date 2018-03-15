package client;

import client.message.Message;

import java.util.ArrayList;

public class MessageManager implements Observer, Subject, ClientObserver {
    private Message message;
    private Message userMessage;
    private ArrayList<Observer> observers;

    public MessageManager() {
        observers = new ArrayList<>();
    }


    @Override
    public void update(Message message) {
        this.message = message;
        display();
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
            this.userMessage = userMessage;
            for (Observer observer: observers) {
                observer.update(userMessage);
            }

    }


}