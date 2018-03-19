package server;

import messageSystem.AuthMessage;
import messageSystem.BroadcastMessage;
import messageSystem.Message;
import messageSystem.MessageManager;
import org.junit.*;
import server.clientData.Session;
import server.clientData.User;
import server.clientData.UserSessionManager;
import server.clientData.UsersManager;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by Денис on 08.03.2018.
 */
public class RegistrationTest {

    private static Server srv;

    private String message;
    private Client cl2;
    private ArrayList<Client> clients;
    @BeforeClass
    public static void runServer () {
       Thread serverThread = new Thread(() ->{
           srv = new Server(8190);
           srv.start();
       });
       serverThread.start();
        UsersManager manager = UsersManager.getInstance();


    }
    @Before
    public void setUp() throws Exception {

        message = "testMessage";


    }
    @After
    public void tearDown() throws Exception {
        UsersManager manager = UsersManager.getInstance();
        manager.deleteUser(manager.isRegistered("aaa","aaa"));
        manager.deleteUser(manager.isRegistered("repeatedLoginAfterBreakConnection",
                "repeatedLoginAfterBreakConnection"));
        manager.deleteUser(manager.isRegistered("failedDuplicateLogin",
                "failedDuplicateLogin"));
        manager.deleteUser(manager.isRegistered("c1",
                "c1"));
        manager.deleteUser(manager.isRegistered("c2",
                "c2"));
        if(clients != null) {
            for (Client c : clients) {
                manager.deleteUser(manager.isRegistered(c.getUsername(), c.getPass()));
            }
        }
        manager.deleteUser(manager.isRegistered("c01", "c01"));

    }
    @AfterClass
    public  static void deletingTestData() {
        
    }

    @Test
    public void registration() throws Exception {
        System.out.println("Run test registration");
        Client client = new Client(8190);
        Message msg = new AuthMessage(String.valueOf(Math.random()), "aaa",
                "aaa");
        client.write(msg);
        Message srv = client.read();
        AuthMessage auth = (AuthMessage)srv;
        assertEquals("aaa",auth.getUserlogin());
    }
    @Ignore
    @Test
    public void repeatedLoginAfterBreakConnection() throws IOException {
        System.out.println("Run test repeatedLoginAfterBreakConnection");
        Client c1 = new Client(8190);
        Message msg = new AuthMessage(String.valueOf(Math.random()), "repeatedLoginAfterBreakConnection",
                "repeatedLoginAfterBreakConnection");
        c1.write(msg);

        Message srv1 = c1.read();
        c1.socket.close();
        Client c2 = new Client(8190);
        c2.write(msg);
        Message srv = c2.read();
        AuthMessage auth = (AuthMessage)srv;
        assertEquals("repeatedLoginAfterBreakConnection",auth.getUserlogin());

    }
    @Test
    public void failedDuplicateLogin () throws IOException {
        Client client = new Client(8190);
        System.out.println("Run test failedDuplicateLogin");
        Message msg = new AuthMessage(String.valueOf(Math.random()), "failedDuplicateLogin",
                "failedDuplicateLogin");
        client.write(msg);
        Message srv1 = client.read();
        cl2 = new Client(8190);
        cl2.write(msg);
        Message answ = cl2.read();
        AuthMessage auth = (AuthMessage)answ;
        assertNull(auth.getUserid());
    }
     @Ignore
    @Test
    public void sendingBroadcastMessage() throws IOException {
        Client client = new Client(8190);
        System.out.println("Run test sendingBroadcastMessage");
        Client c2 = new Client(8190);
        Client c1 = client;
        Message msg1 = new AuthMessage(String.valueOf(Math.random()), "c1",
                "c1");
        c1.write(msg1);
        Message auth = c1.read();
        assertNotNull(((AuthMessage )auth).getUserid());
        Message msg2 = new AuthMessage(String.valueOf(Math.random()), "c2",
                "c2");
        c2.write(msg2);
        auth = c2.read();
        assertNotNull(((AuthMessage )auth).getUserid());
        c1.write(new BroadcastMessage("BroadcastMessage form c1", "c1"));
        Message message = c2.read();
        assertEquals("BroadcastMessage form c1", ((BroadcastMessage) message).getText());
    }

    /**
     * Создаёт заданное количество клиентов и одного контрольного клиента, который читает сообщения от остальных клиентов
     * @throws IOException
     */
    @Test
    public void simpleLoad() throws IOException {
        System.out.println("simpleLoad");
        clients = new ArrayList<>(100);
        for (int i =1; i < 10; i++) {
            Client c = new Client(8190);
            String str = String.valueOf(Math.random());
            c.setUsername(str);
            c.setPass(str);
            clients.add(c);
        }
        for (Client c: clients) {
            Message msg = new AuthMessage(String.valueOf(Math.random()), c.getUsername(),
                   c.getPass());
            c.write(msg);
            c.read();
        }
        Client c1 = new Client(8190);
        Message msg1 = new AuthMessage(String.valueOf(Math.random()), "c01",
                "c01");
        c1.write(msg1);
        Message auth = c1.read();
        for (Client c: clients) {
            System.out.println("Sending test messages");
            c.write(new BroadcastMessage("BroadcastMessage for c01", c.getUsername()));
            Message m =c1.read();
            System.out.println(m);
            assertEquals("BroadcastMessage for c01", ((BroadcastMessage)m).getText());
        }
        System.out.println("test end");
    }
}
