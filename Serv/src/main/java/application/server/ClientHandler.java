package application.server;

import application.messageSystem.MessageFactory;
import application.messageSystem.MessageEvent;
import application.messageSystem.MessagePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import application.server.clientData.User;
import application.messageSystem.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;


import static application.server.Server.logger;


/**
 * Перехватывает сообщения от подключившегося клиента и помещает их в пул для дальнейшей обработки
 * Created by Денис on 06.03.2018.
 */
@Component("clientHandler")
@DependsOn("server")
@Scope("prototype")
public class ClientHandler implements Runnable {
    @Value("#{server.clientId}")
    private int handlerId;
    @Value("#{server.clientSocket}")
    private Socket clientSocket;
    private ObjectOutputStream out;
    private final MessagePool messagePool;
    private User user;

    @Autowired
    ClientHandler(final MessagePool messagePool) {
        this.messagePool = messagePool;
    }

    @Override
    public void run() {
        try {
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (final IOException e) {
            e.printStackTrace();
        }
        try (InputStream inputStream = clientSocket.getInputStream();
             ObjectInputStream oin = new ObjectInputStream(inputStream)) {
            while (!clientSocket.isClosed()) {
                final Message clientMessage = (Message) oin.readObject();
                {
                    messagePool.addMessage(new MessageEvent(this, clientMessage));
                }
            }
        } catch (final EOFException e) {
            //logger.log(Level.WARNING, handlerId +e.getMessage(), e);
        } catch (final SocketException e) {

            logger.log(Level.WARNING, e.getMessage(), e.getMessage());
        } catch (final ClassNotFoundException | IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e.getCause());
        } finally {
            final Message m = MessageFactory.createSystemMessage("clearSession");
            messagePool.addMessage(new MessageEvent(this, m));
        }
    }

    /**
     * Передаёт объект сообщения
     *
     * @param message Объект собщения.
     */
    public void printMessage(final Message message) {
        try {
            logger.log(Level.FINER, "{0} sending message: {1}",
                    new Object[]{this.getClass().getSimpleName(), message.toString()});
            if (!clientSocket.isClosed()) {
                out.writeObject(message);
                out.flush();
                logger.log(Level.FINER, "{0} message sent:  {1}",
                        new Object[]{this.getClass().getSimpleName(), message.toString()});
            } else {
                logger.log(Level.WARNING, "{0} can't send message: socket was closed",
                        new Object[]{this.getClass().getSimpleName()});
            }
        } catch (final IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }


    @Override
    public String toString() {
        return "ClientHandler{" +
                "handlerId=" + handlerId +
                ", user=" + user +
                '}';
    }

    public int getHandlerId() {
        return handlerId;
    }
}
