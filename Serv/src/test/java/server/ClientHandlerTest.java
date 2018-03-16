package server;

import client.message.Message;
import org.junit.*;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by Денис on 08.03.2018.
 */
public class ClientHandlerTest {

    Client client;
    String message;
    Thread serverThread;
   @BeforeClass
    public static void runServer () {
       Thread serverThread = new Thread(() -> new Server(8190));
       serverThread.start();
   }

    @Before
    public void setUp() throws Exception {
        client = new Client(8190);
        message = "testMessage";
    }

    @After
    public void tearDown() throws Exception {
        serverThread = null;
        client.socket.close();
        client=null;
        message = null;
    }

    @Test
    public void registration() throws Exception {
        Message msg = new Message("aaa:aaa", "system",
                LocalDate.now(), LocalTime.now());
        msg.setMessageType("auth");
        client.write(msg);
        Message srv = client.read();
        assertEquals("aaa",srv.getUserName());
    }
    @Test
    public void repeatedLoginAfterBreakConnection() throws IOException {
       Message msg = new Message(
                "repeatedLoginAfterBreakConnection:repeatedLoginAfterBreakConnection",
                "system",
                LocalDate.now(),
                LocalTime.now());
        msg.setMessageType("auth");
        client.write(msg);
        Message srv1 = client.read();
        System.out.println(srv1);
        client.socket.close();
        client = new Client(8190);
        client.write(msg);
        Message srv = client.read();
        assertEquals("repeatedLoginAfterBreakConnection",srv.getUserName());
    }
    /*@Test
    public void repeatedLoginAfterUserExit() throws IOException {
        client.read();
        client.write("repeatedLoginAfterUserExit");
        client.read();
        client.write("repeatedLoginAfterUserExit");
        client.read();
        client.write("exit");
        client = new Client(8190);
        client.read();
        client.write("repeatedLoginAfterUserExit");
        client.read();
        client.write("repeatedLoginAfterUserExit");
        String s = client.read();
        assertEquals("Hello, repeatedLoginAfterUserExit!!!",s);
    } */
    @Test
    public void failedDuplicateLogin () throws IOException {
        Message msg = new Message(
                "failedDuplicateLogin:failedDuplicateLogin",
                "system",
                LocalDate.now(),
                LocalTime.now());
        msg.setMessageType("auth");
        client.write(msg);
        Message srv1 = client.read();
        System.out.println(srv1);
        Client cl2 = new Client(8190);
        cl2.write(msg);
        Message answ = cl2.read();
        assertEquals("User allready loggined",answ.getText());
    }
   /* @Test
    public void ignoreBackspaseInLogin (){

    }*/
}
class Client {
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    public Client (int port) throws IOException {
        socket = new Socket("localhost", port);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
    }
    public Message read (){
        try {
            return (Message) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null ;
    }

    public void write (Message msg) {
        try {
            out.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}