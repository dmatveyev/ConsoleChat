package client;

import client.message.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

/**Принимает входящее сообщение  дессериализует
 * Created by Денис on 06.03.2018.
 */
public class SocketReader implements Runnable, Subject {
    private ObjectInputStream in;
    private ArrayList<Observer> observers;
    private Message message;
    public SocketReader(ObjectInputStream in) {
        this.in = in;
        observers = new ArrayList<>();
    }


    @Override
    public void run() {
        try {
            //Не уверен в условии чтения.
            while (true) {
                message = (Message) in.readObject();
                notifyObservers();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
}
