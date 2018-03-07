package server;



import server.clientData.User;
import server.clientData.UsersManager;
import server.messagePool.Message;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
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
    private UsersManager usersManager;



    public ClientHandler(Server server, Socket clientSoket, int clientId) {
        this.clientId = clientId;
        this.clientSocket = clientSoket;
        this.server = server;
        this.usersManager = UsersManager.getInstance();
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
                if (line.equals("exit")) {
                    User removed = usersManager.getActiveUser(String.valueOf(clientId));
                    if (removed != null)
                         usersManager.removeActiveUser(usersManager.removeUserSession(removed));
                    clientSocket.close();
                } else {
                    Message message = new Message(line, usersManager.getRegisteredUser(String.valueOf(clientId)), LocalDate.now(), LocalTime.now());
                    server.sendMessageToAll(message.toString());
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void printMessage(String message) {
        out.println(message);
    }

    public void authorize(Scanner reader, PrintWriter writer) throws IOException {
        writer.println("Hello, please enter login:");
        String login = "";
        String pass= "";

        if (reader.hasNextLine()){
             login = reader.nextLine();
        }

        String userId = usersManager.isRegistered(login);
        if (userId != null){
            User registeredUser = usersManager.getRegisteredUser(userId);
            if(registeredUser.getSession()!= null) {
                out.println("You allredy loggined, connection has been closed");
                throw new IOException("User allredy loggined");
            }
            writer.println("Hello, please enter password:");
            if (reader.hasNextLine()){
                pass = reader.nextLine();
                while (!registeredUser.getPassword().equals(pass)){
                    out.println("Wrong password");
                    writer.println("Hello, please enter password:");
                    pass = reader.nextLine();
                }
                    out.printf("Hello %s !!!", login);
                    out.println();
                    usersManager.createUserSession(registeredUser);
                    usersManager.addActiveUser(registeredUser);
                    clientId = Integer.parseInt(registeredUser.getUserId());
            }
        }else{
            writer.println("Hello, please enter password:");
            if (reader.hasNextLine()) {
                pass = reader.nextLine();
            }
            User newuser = usersManager.createUser(String.valueOf(clientId),login,pass);
            usersManager.createUserSession(newuser);
            usersManager.addActiveUser(newuser);
            out.printf("Hello %s !!!", login);
            out.println();
        }
    }
}
