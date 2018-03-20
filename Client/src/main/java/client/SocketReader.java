package client;

import messageSystem.Message;

import java.io.IOException;
import java.io.ObjectInput;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static client.Client.logger;

/**Принимает входящее сообщение  дессериализует
 * Created by Денис on 06.03.2018.
 */
public class SocketReader implements Runnable, Subject {
    private final ObjectInput in;
    private final List<Observer> observers;
    private Message message;
    SocketReader(final ObjectInput in) {
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
        }
        catch ( final SocketException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }catch (IOException | ClassNotFoundException e) {
            logger.log(Level.WARNING, e.getMessage(), e.getCause());
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
        for (final Observer observer: observers) {
            observer.update(message);
        }

    }
}
