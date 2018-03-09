package server;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by Денис on 08.03.2018.
 */
public class ClientHandlerTest {
    Server server;
    Client client;
    String login;
    String password;
    String message;

    @BeforeClass
    public static void runServer () {
       Thread serverThread = new Thread(() -> new Server(8190));
       serverThread.start();
    }

    @Before
    public void setUp() throws Exception {
        client = new Client(8190);
        login = "testLogin";
        password = "testPassword";
        message = "testMessage";
    }

    @After
    public void tearDown() throws Exception {
        client.socket.close();
        login = null;
        password = null;
        message = null;
    }

    @Test
    public void registration() throws Exception {
        client.read();
        client.write(login);
        client.read();
        client.write(password);
        String s = client.read();
        assertEquals("Hello, "+login + "!!!",s);

    }
}
class Client {
    Socket socket;
    Scanner in;
    PrintWriter out;
    public Client (int port) throws IOException {
        socket = new Socket("localhost", port);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream())
                , true);
    }
    public String read (){
        String str="";
        try {
            if (in.hasNextLine())
                str = in.nextLine();
        } catch (Exception e) {
        e.printStackTrace();
    }
        return str;
    }

    public void write (String str) {
        try  {
            out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}