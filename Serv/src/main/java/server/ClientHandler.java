package main.java.server;

import main.java.server.messagePool.Message;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

/**
 * Created by Денис on 06.03.2018.
 */
public class ClientHandler implements Runnable {
    private Server server;
    private Socket clientSocket;
    private int clientId;
    private PrintWriter out;


    public ClientHandler(Server server, Socket clientSoket, int clientId) {
        this.clientId = clientId;
        this.clientSocket = clientSoket;
        this.server = server;
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
            out.println("Hello " + clientId + "!!!");
            while (in.hasNextLine()) {
                String line = in.nextLine();
                Message message = new Message(line, clientId, LocalDate.now(), LocalTime.now());
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
}
