package server;

import server.clientData.AbstractUser;
import server.clientData.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Денис on 06.03.2018.
 */
public class Server {

    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();

    public ArrayList<User> getUsers() {
        return users;
    }

    private ArrayList<User> users = new ArrayList<>();

    private int clientId;

    public Server(int port) {
        users.add(createAdmin());
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Spawing " + ++clientId);
                ClientHandler clientHandler = new ClientHandler(this,clientSocket, clientId);
                clients.add(clientHandler);
                Thread t = new Thread(clientHandler);
                t.start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private User createAdmin() {
        User admin = new User();
        admin.setLogin("admin");
        admin.setPassword("password");
        return admin;
    }

    public void sendMessageToAll(String message) {
        for(ClientHandler c: clients){
            c.printMessage(message);
        }
    }
}
