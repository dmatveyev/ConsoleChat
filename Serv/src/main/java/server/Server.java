package server;



import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Денис on 06.03.2018.
 */
public class Server {

    private ArrayList<ClientHandler> clients = new ArrayList<>();

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int clientId;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.printf("Server started on port %s", port);
            System.out.println();
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("Spawing " + ++clientId);
                ClientHandler clientHandler = new ClientHandler(this, clientSocket);
                clients.add(clientHandler);
                Thread t = new Thread(clientHandler);
                t.start();
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }
    public void sendMessageToAll(String message) {
        for(ClientHandler c: clients){
            c.printMessage(message);
        }
    }
}
