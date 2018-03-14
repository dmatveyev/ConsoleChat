package client;

import java.io.IOException;

import java.net.Socket;
import java.net.UnknownHostException;


/**Реализация клиента
 * Created by Денис on 06.03.2018.
 */
public class Client {

    public Client(int port) {
        try {
            Socket clientS = new Socket("localhost", port);
            Thread read = new Thread (new SocketReader(clientS.getInputStream()));
            Thread write = new Thread (new SocketWriter(clientS.getOutputStream()));
            read.start();
            write.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            if (e.getMessage().contains("refused"))
                System.err.println("Connetcion Refused");
            else e.printStackTrace();

        }
    }
}
