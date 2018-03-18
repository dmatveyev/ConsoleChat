package server;



import messageSystem.MessageFactory;
import messageSystem.MessageManager;
import messageSystem.MessagePool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Денис on 06.03.2018.
 */
public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int clientId;
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.printf("Server started on port %s\n", port);
            MessageFactory messageFactory = new MessageFactory();
            MessagePool messagePool = MessagePool.getInstance();
            MessageManager messageManager = new MessageManager(messageFactory);
            messagePool.registerManager(messageManager);
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("Spawing " + ++clientId);
                ClientHandler clientHandler = new ClientHandler(clientId, clientSocket,messageFactory);
                messageManager.addHandler(clientId,clientHandler);
                Thread t = new Thread(clientHandler);
                t.start();
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
