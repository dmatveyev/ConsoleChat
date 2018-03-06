package client;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Денис on 06.03.2018.
 */
public class SocketWriter implements Runnable {
    Socket clientS;
    public SocketWriter(Socket clientS) {
        this.clientS = clientS;
    }

    @Override
    public void run() {
        try (Scanner in = new Scanner(System.in);
             PrintWriter out = new PrintWriter(
                     new OutputStreamWriter(clientS.getOutputStream())
                     , true)) {
            while(in.hasNextLine()){
                out.println(in.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
