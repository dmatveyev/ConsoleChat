package server;

import client.message.MessagePair;
import client.message.MessagePool;
import server.clientData.User;
import server.clientData.UserSessionManager;
import server.clientData.UsersManager;
import client.message.Message;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;

/**        asdf
 * Created by Денис on 06.03.2018.
 */
public class ClientHandler implements Runnable {
    private int handlerId;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private MessagePool messagePool;
    private User user;
    public ClientHandler(int handlerId, Socket clientSocket) {
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
            Message clearUserSession = new Message(
                    String.valueOf("clearSession"),
                    "system",
                    LocalDate.now(),
                    LocalTime.now());
            clearUserSession.setMessageType("clearSession");
            messagePool.addMessage(new MessagePair(handlerId,clearUserSession));
        }
    }
    public void printMessage(Message message) {
        try {
            System.out.println ("sending message");
            out.writeObject(message);
            out.flush();
            System.out.println ("message sent");
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
