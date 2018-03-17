package server;

import messageSystem.AuthMessage;
import messageSystem.BroadcastMessage;
import messageSystem.Message;
import org.junit.*;
import server.clientData.Session;
import server.clientData.User;
import server.clientData.UserSessionManager;
import server.clientData.UsersManager;

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
        client = null;
        message = null;
    }
    @AfterClass
    public  static void deletingTestData() {
        UsersManager manager = UsersManager.getInstance();
        manager.deleteUser(manager.isRegistered("aaa","aaa"));
        manager.deleteUser(manager.isRegistered("repeatedLoginAfterBreakConnection",
                "repeatedLoginAfterBreakConnection"));
        manager.deleteUser(manager.isRegistered("failedDuplicateLogin",
                "failedDuplicateLogin"));
    }

    @Test
    public void registration() throws Exception {
        Message msg = new AuthMessage(String.valueOf(Math.random()), "aaa",
                "aaa");
        client.write(msg);
        Message srv = client.read();
        AuthMessage auth = (AuthMessage)srv;
        assertEquals("aaa",auth.getUserlogin());
    }
    @Test
    public void repeatedLoginAfterBreakConnection() throws IOException {
        Message msg = new AuthMessage(String.valueOf(Math.random()), "repeatedLoginAfterBreakConnection",
                "repeatedLoginAfterBreakConnection");
        client.write(msg);
        Message srv1 = client.read();
        client.socket.close();
        client = new Client(8190);
        client.write(msg);
        Message srv = client.read();
        AuthMessage auth = (AuthMessage)srv;
        assertEquals("repeatedLoginAfterBreakConnection",auth.getUserlogin());

    }
    @Test
    public void failedDuplicateLogin () throws IOException {
        Message msg = new AuthMessage(String.valueOf(Math.random()), "failedDuplicateLogin",
                "failedDuplicateLogin");
        client.write(msg);
        Message srv1 = client.read();
        Client cl2 = new Client(8190);
        cl2.write(msg);
        Message answ = cl2.read();
        AuthMessage auth = (AuthMessage)answ;
        assertNull(auth.getUserid());
    }
    @Test
    public void sendingBroadcastMessage() throws IOException {
        Client c1 = new Client(8190);
        Client c2 = new Client(8190);
        Message msg1 = new AuthMessage(String.valueOf(Math.random()), "c1",
                "c1");
        Message msg2 = new AuthMessage(String.valueOf(Math.random()), "c2",
                "c2");
        c1.write(msg1);
        c2.write(msg2);
        c1.write(new BroadcastMessage("BroadcastMessage", "c1"));
        Message message = c2.read();
        BroadcastMessage b = (BroadcastMessage)message;
        System.out.println(b);
        assertEquals("BroadcastMessage", b.getText());
    }
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