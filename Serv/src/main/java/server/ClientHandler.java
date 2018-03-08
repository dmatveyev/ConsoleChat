package server;



import server.clientData.User;
import server.clientData.UserSessionManager;
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
    private UserSessionManager userSessionManager;




    public ClientHandler(Server server, Socket clientSoket, int clientId) {
        this.clientId = clientId;
        this.clientSocket = clientSoket;
        this.server = server;
        this.usersManager = UsersManager.getInstance();
        this.userSessionManager = UserSessionManager.getInstance();
        try {
            this.out  = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try(InputStream inputStream = clientSocket.getInputStream()) {
            Scanner in = new Scanner(inputStream, "UTF-8");
            authorize(in, out);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.equals("exit")) {
                    String session = userSessionManager.isActive(String.valueOf(clientId));
                    userSessionManager.doUnactive(session);
                    clientSocket.close();
                    out.close();
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
            if(userSessionManager.isActive(userId) != null) {
                writer.println("You allredy loggined, connection has been closed");
                throw new IOException("User allredy loggined");
            }
            writer.println("Hello, please enter password:");
            if (reader.hasNextLine()){
                pass = reader.nextLine();
                while (!registeredUser.getPassword().equals(pass)){
                    writer.println("Wrong password");
                    writer.println("Hello, please enter password:");
                    pass = reader.nextLine();
                }
                    writer.printf("Hello %s !!!", login);
                    writer.println();
                    String userSession = userSessionManager.createUserSession(registeredUser);
                    userSessionManager.doActive(userSession, userId);
                    clientId = Integer.parseInt(registeredUser.getUserId());
            }
        }else{
            writer.println("Hello, please enter password:");
            if (reader.hasNextLine()) {
                pass = reader.nextLine();
            }
            User newuser = usersManager.createUser(String.valueOf(clientId),login,pass);
            String userSession = userSessionManager.createUserSession(newuser);
            userSessionManager.doActive(userSession, String.valueOf(clientId));
            writer.printf("Hello %s !!!", login);
            writer.println();
        }
    }
}
