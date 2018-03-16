package server;

import messageSystem.AuthMessage;
import messageSystem.Message;
import org.junit.*;
import server.clientData.UsersManager;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;

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
        Message msg = new AuthMessage("aaa:aaa", "system",
                LocalDate.now(), LocalTime.now());
        msg.setMessageType("auth");
        client.write(msg);
        Message srv = client.read();
        assertEquals("aaa",srv.getUserName());
    }
    @Test
    public void repeatedLoginAfterBreakConnection() throws IOException {
       Message msg = new AuthMessage(
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
    @Test
    public void failedDuplicateLogin () throws IOException {
        Message msg = new AuthMessage(
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