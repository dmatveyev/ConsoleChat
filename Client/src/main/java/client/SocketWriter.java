package client;

import messageSystem.Message;

import java.io.*;
import java.util.logging.Level;

import static client.Client.logger;


/**Отвечает за серриализацию и отправку объекта Message
 * Created by Денис on 06.03.2018.
 */
public class SocketWriter implements Observer {
    private final ObjectOutput outputStream;
    SocketWriter(final ObjectOutput outputStream) {
        this.outputStream = outputStream;
    }
    private void write(final Message message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (final IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }
    @Override
    public void update(final Message message) {
        write(message);
    }
}
