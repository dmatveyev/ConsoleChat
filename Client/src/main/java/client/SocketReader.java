package main.java.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Денис on 06.03.2018.
 */
public class SocketReader implements Runnable {
    private Socket clientS;
    public SocketReader(Socket clientS) {
        this.clientS = clientS;
    }
    @Override
    public void run() {
        try(Scanner in = new Scanner(clientS.getInputStream())) {
            while (in.hasNextLine())
                System.out.println(in.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
