package application.server;

import application.messageSystem.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;


/**
 * Created by Денис on 17.03.2018.
 */
class Client {
    private static final Logger logger = Logger.getLogger("Tests");
    private String username;
    private String pass;
    Socket socket;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    Client(final int port) {
        try {
            socket = new Socket("localhost", port);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (final UnknownHostException e) {
            logger.warning(e.toString());
        } catch (final IOException e) {
            logger.warning(e.getCause().toString());
        }
    }

    String getUsername() {
        return username;
    }

    void setUsername(final String username) {
        this.username = username;
    }

    String getPass() {
        return pass;
    }

    void setPass(final String pass) {
        this.pass = pass;
    }

    Message read() {
        try {
            return (Message) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.warning(e.toString());
        }
        return null;
    }

    void write(final Message msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (final IOException e) {
            logger.warning(e.toString());
        }
    }

}
