package client;


import messageSystem.MessageFactory;
import messageSystem.User;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**Реализация клиента
 * Created by Денис on 06.03.2018.
 */
public class Client {

    public static final Logger logger = Logger.getLogger("Client");

    private final int port;
    private final UserMessageReader userMessageReader;
    private ObjectInput in = null;
    private ObjectOutput out = null;
    private final MessageManager manager;

    public Client(final int port) {
        this.port = port;
        final User user = new User();
        manager = new MessageManager(user);
        userMessageReader = new UserMessageReader(user);
        userMessageReader.registerObserver(manager);
    }

    void start() {
        try (Socket clientS = new Socket("localhost", port)){
            in = new ObjectInputStream(clientS.getInputStream());
            out = new ObjectOutputStream(clientS.getOutputStream());
        } catch (final UnknownHostException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        } catch (final IOException e) {
             logger.log(Level.WARNING, e.getMessage(), e.getCause());
        }
        final SocketReader reader =  new SocketReader(in);
        new SocketWriter(out);
        reader.registerObserver(manager);
        final Thread read = new Thread (reader);
        read.start();
        userMessageReader.read();
    }
}
