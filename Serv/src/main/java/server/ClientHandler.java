package server;



import server.clientData.User;
import server.messagePool.Message;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Денис on 06.03.2018.
 */
public class ClientHandler implements Runnable {
    private Server server;
    private Socket clientSocket;
    private int clientId;
    private PrintWriter out;
    private Map<String,User> users;
    private User user;


    public ClientHandler(Server server, Socket clientSoket, int clientId) {
        this.clientId = clientId;
        this.clientSocket = clientSoket;
        this.server = server;
        this.users = server.getUsers();
        try {
            this.out = new PrintWriter(new OutputStreamWriter(clientSoket.getOutputStream(), "UTF-8"), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try(InputStream inputStream = clientSocket.getInputStream()
            ) {
            Scanner in = new Scanner(inputStream, "UTF-8");
            authorize(in, out);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                Message message = new Message(line, user.getLogin(), LocalDate.now(), LocalTime.now());
                server.sendMessageToAll(message.toString());
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void printMessage(String message) {
        out.println(message);
    }

    public void authorize(Scanner reader, PrintWriter writer) {
        writer.println("Hello, please enter login:");
        String login = "";
        String pass= "";
        user = new User();
        if (reader.hasNextLine()){
             login = reader.nextLine();
        }
        writer.println("Hello, please enter password:");
        if (reader.hasNextLine()){
            pass = reader.nextLine();
        }
        for (Map.Entry<String, User> u: users.entrySet()) {
           if (!(u.getValue().getLogin().equals(login)&&u.getValue().getPassword().equals(pass))) {
               user.setLogin(login);
               user.setPassword(pass);
           }else {
               out.println("You are already connected");
               try {
                   clientSocket.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
        }
        if (user.getLogin() != null) {
            users.put(String.valueOf(clientId), user);
        }
        writer.println("Hello " + user.getLogin());

    }
}
