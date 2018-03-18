package server;

import messageSystem.MessageFactory;
import messageSystem.MessagePair;
import messageSystem.MessagePool;
import server.clientData.User;
import messageSystem.Message;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;

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
        }
        catch (IOException e) {
            //System.err.printf ("Server error message: %s", e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
            System.out.println ("sending message: " + message.toString());
            out.writeObject(message);
            out.flush();
            System.out.println ("message sent: " + message.toString());
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
}
