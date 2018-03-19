package client;

import messageSystem.Message;
import messageSystem.MessageFactory;
import messageSystem.User;

import java.util.ArrayList;
import java.util.Scanner;

public class UserMessageReader implements ClientSubject {
    private final User user;
    private Message userMessage;
    private ArrayList<ClientObserver> observers;
    private Scanner in;
    private MessageFactory messageFactory;

    public UserMessageReader (User user, MessageFactory messageFactory) {
        this.user = user;
        this.messageFactory = messageFactory;
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