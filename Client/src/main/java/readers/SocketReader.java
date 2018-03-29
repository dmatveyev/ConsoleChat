package readers;

import application.messageSystem.Message;

import java.io.IOException;
import java.io.ObjectInput;
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
    public SocketReader(final ObjectInput in) {
        this.in = in;
        observers = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            //Не уверен в условии чтения.
            while (true) {
                read();
                notifyObservers(message);
            }

        }catch (IOException | ClassNotFoundException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    public Message read() throws IOException, ClassNotFoundException {
        message = (Message) in.readObject();
        return  message;

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
        for (final Observer observer: observers) {
            observer.update(this.message);
        }

    }
}
