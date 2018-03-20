package client;

import messageSystem.Message;
import messageSystem.MessageFactory;
import messageSystem.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserMessageReader implements ClientSubject, Runnable {
    private final User user;
    private Message userMessage;
    private final List<ClientObserver> observers;
    private final Scanner in;

    UserMessageReader(final User user) {
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
        for (final ClientObserver observer: observers) {
            observer.updateClient(userMessage);
        }
    }
    @Override
    public void run() {
        System.out.println ("Enter Login");
        String login = "";
        if (in.hasNextLine())
            login=in.nextLine();
        System.out.println ("Enter password");
        String password = "";
        if (in.hasNextLine())
            password =in.nextLine();
        user.setLogin(login);
        user.setPassword(password);
        userMessage = MessageFactory.createAuthMessage(null,
               login,
                password);
        notifyObservers();
        //переключаемся на чтение обычных сообщений
        while (in.hasNextLine()){
            userMessage = MessageFactory.createBroadcastMessage(
                    in.nextLine(),
                    user.getLogin());
            notifyObservers();
        }
    }
}