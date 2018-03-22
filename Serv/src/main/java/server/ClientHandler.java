package server;

import messageSystem.MessageFactory;
import messageSystem.MessagePair;
import messageSystem.MessagePool;
import server.clientData.User;
import messageSystem.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;


import static server.Server.logger;


/**
 * Перехватывает сообщения от подключившегося клиента и помещает их в пул для дальнейшей обработки
 * Created by Денис on 06.03.2018.
 */
public class ClientHandler implements Runnable {

    private final int handlerId;
    private final Socket clientSocket;
    private ObjectOutputStream out;
    private final MessagePool messagePool;
    private User user;

    ClientHandler(final int handlerId, final Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.handlerId = handlerId;
        messagePool = MessagePool.getInstance();
        try {
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try (InputStream inputStream = clientSocket.getInputStream();
             ObjectInputStream oin = new ObjectInputStream(inputStream)) {
            while (!clientSocket.isClosed()) {
                final Message clientMessage = (Message) oin.readObject();
                {
                    final MessagePair pair = new MessagePair(handlerId, clientMessage);
                    messagePool.addMessage(pair);
                }
            }
        } catch (final EOFException e) {
            //logger.log(Level.WARNING, handlerId +e.getMessage(), e);
        } catch (final SocketException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        } catch (final ClassNotFoundException | IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e.getCause());
        } finally {
            final Message m = MessageFactory.createSystemMessage("clearSession");
            messagePool.addMessage(new MessagePair(handlerId, m));
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
}
