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
public class RegistrationTest {

    Client client;
    String message;

    @BeforeClass
    public static void runServer () {
       Thread serverThread = new Thread(() ->{
          Server srv = new Server(8190);
          srv.start();
       });
       serverThread.start();
    }
    @Before
    public void setUp() throws Exception {
        client = new Client(8190);
        message = "testMessage";
    }
    @After
    public void tearDown() throws Exception {

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
        System.out.println("Run test registration");
        Message msg = new AuthMessage(String.valueOf(Math.random()), "aaa",
                "aaa");
        client.write(msg);
        Message srv = client.read();
        AuthMessage auth = (AuthMessage)srv;
        assertEquals("aaa",auth.getUserlogin());
    }
    @Test
    public void repeatedLoginAfterBreakConnection() throws IOException {
        System.out.println("Run test repeatedLoginAfterBreakConnection");
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
        System.out.println("Run test failedDuplicateLogin");
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

}
