package application.messageSystem;

import readers.ClientObserver;
import readers.Observer;
import readers.Subject;

import java.util.ArrayList;
import java.util.List;

public class MessageManager implements Observer, Subject, ClientObserver {
    private final User user;

    private final List<Observer> observers;

    public MessageManager(final User user) {
        this.user = user;
        observers = new ArrayList<>();
    }


    private void doMessage(final Message message) {
        if (message instanceof BroadcastMessage)
            display(message);
        if (message instanceof AuthMessage) {
            user.setUserId(((AuthMessage) message).getUserPassword());
            System.out.printf("Hello, %s!!!\n", user.getLogin());
        }
    }

    @Override
    public void update(final Message message) {
        doMessage(message);
    }

    private void display(final Message message) {
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
    public void notifyObservers(final Message message) {
        for (final Observer observer : observers) {
            observer.update(message);
        }
    }

    @Override
    public void updateClient(final Message message) {

        for (final Observer observer : observers) {
            observer.update(message);
        }

    }


}