package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**Отвечает за серриализацию и отправку объекта UserMessage
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
             ObjectOutputStream out = new ObjectOutputStream(clientS.getOutputStream())){
            String text= "";
            while(in.hasNextLine()){
               text =in.nextLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
