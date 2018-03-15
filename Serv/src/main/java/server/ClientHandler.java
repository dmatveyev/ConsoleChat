package server;

import client.message.MessagePool;
import server.clientData.UserSessionManager;
import server.clientData.UsersManager;
import client.message.Message;
import java.io.*;
import java.net.Socket;

/**        asdf
 * Created by Денис on 06.03.2018.
 */
public class ClientHandler implements Runnable {
    private Server server;
    private Socket clientSocket;
    private ObjectOutputStream out;

    private UsersManager usersManager;
    private UserSessionManager userSessionManager;

    public ClientHandler(Server server, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.usersManager = UsersManager.getInstance();
        this.userSessionManager = UserSessionManager.getInstance();
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
                    MessagePool messagePool = MessagePool.getInstance();
                    messagePool.addMessage(clientMessage);
                }

                //server.sendMessageToAll(clientMessage);
            }
        } catch (IOException e) {
            System.err.printf ("Server error message: %s", e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {

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
}
