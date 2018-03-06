package client;

import java.io.IOException;

import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Created by Денис on 06.03.2018.
 */
public class Client {

    public Client(int port) {
        try {
            Socket clientS = new Socket("localhost", port);
            Thread read = new Thread (new SocketReader(clientS));
            Thread write = new Thread (new SocketWriter(clientS));
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
