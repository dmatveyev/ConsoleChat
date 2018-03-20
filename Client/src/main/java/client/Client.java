package client;

import messageSystem.User;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Реализация клиента
 * Created by Денис on 06.03.2018.
 */
public class Client {

    static final Logger logger = Logger.getLogger("Client");
    private Socket clientS = null;
    private final int port;
    private ObjectInput in = null;
    private ObjectOutput out = null;


    public Client(final int port) {
        this.port = port;
    }

    void start() {
        final User user = new User();
        final MessageManager manager = new MessageManager(user);
        final UserMessageReader userMessageReader = new UserMessageReader(user);

        try  {
            clientS = new Socket("localhost", port);
            in = new ObjectInputStream(clientS.getInputStream());
            out = new ObjectOutputStream(clientS.getOutputStream());
        } catch (final UnknownHostException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        } catch (final IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e.getCause());
        }
        final SocketReader reader = new SocketReader(in);
        final SocketWriter writer = new SocketWriter(out);
        userMessageReader.registerObserver(manager);
        reader.registerObserver(manager);
        manager.registerObserver(writer);
        final Thread read = new Thread(reader);
        read.start();
        userMessageReader.run();
    }
}
