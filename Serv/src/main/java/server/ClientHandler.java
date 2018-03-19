package server;

import messageSystem.MessageFactory;
import messageSystem.MessagePair;
import messageSystem.MessagePool;
import server.clientData.User;
import messageSystem.Message;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;

import static server.Server.logger;

/** Перехватывает сообщения от подключившегося клиента и помещает их в пул для дальнейшей обработки
 * Created by Денис on 06.03.2018.
 */
public class ClientHandler implements Runnable {
    private int handlerId;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private MessagePool messagePool;
    private User user;
    private MessageFactory messageFactory;
    public ClientHandler(int handlerId, Socket clientSocket, MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
        this.clientSocket = clientSocket;
        this.handlerId = handlerId;
        messagePool = MessagePool.getInstance();
        try {
            this.out  = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        try(InputStream inputStream = clientSocket.getInputStream()) {
            ObjectInputStream oin = new ObjectInputStream(inputStream);
            while (!clientSocket.isClosed()) {
                Message clientMessage = (Message) oin.readObject();
                {
                    MessagePair pair = new MessagePair(handlerId,clientMessage);
                    messagePool.addMessage(pair);
                }
            }
        } catch (EOFException e) {
            logger.log(Level.WARNING, handlerId +e.getMessage(), e);
        }
        catch (IOException e) {
            //System.err.printf ("Server error message: %s", e.getMessage());
            logger.log(Level.WARNING, e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }finally {
            Message m = messageFactory.createSystemMessage("clearSession");
            messagePool.addMessage(new MessagePair(handlerId,m));
        }
    }

    /**
     * Передаёт объект сообщения
     * @param message Объект собщения.
     */
    public void printMessage(Message message) {
        try {
            logger.log(Level.FINER ,"{0} sending message: {1}",
                    new Object[]{this.getClass().getSimpleName(), message.toString()});
            if (!clientSocket.isClosed()) {
                out.writeObject(message);
                out.flush();
                logger.log(Level.FINER,"{0} message sent:  {1}",
                        new Object[]{this.getClass().getSimpleName(), message.toString()});
            } else {
                logger.log(Level.WARNING,"{0} can't send message: socket was closed",
                        new Object[]{this.getClass().getSimpleName()});
            }
        } catch (IOException e) {
            e.printStackTrace();
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
