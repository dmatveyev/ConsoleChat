package server;

import messageSystem.AuthMessage;
import messageSystem.BroadcastMessage;
import messageSystem.Message;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import server.clientData.UsersManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static server.Main.ctx;
import static server.Main.main;

/**
 * Created by Денис on 08.03.2018.
 */
public class RegistrationTest {
    private static Server srv;
    private List<Client> clients;

    @BeforeClass
    public static void runServer() {
        ctx = new ClassPathXmlApplicationContext("classpath:META-INF/app-context-annotation.xml");
        srv = (Server) ctx.getBean("server");
        final Thread serverThread = new Thread(() -> srv.start());
        serverThread.start();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        final UsersManager manager = (UsersManager) ctx.getBean("userManager");
        manager.deleteUser(manager.isRegistered("aaa", "aaa"));
        manager.deleteUser(manager.isRegistered("repeatedLoginAfterBreakConnection",
                "repeatedLoginAfterBreakConnection"));
        manager.deleteUser(manager.isRegistered("failedDuplicateLogin",
                "failedDuplicateLogin"));
        manager.deleteUser(manager.isRegistered("c1",
                "c1"));
        manager.deleteUser(manager.isRegistered("c2",
                "c2"));
        if (clients != null) {
            for (final Client c : clients) {
                manager.deleteUser(manager.isRegistered(c.getUsername(), c.getPass()));
            }
        }
        manager.deleteUser(manager.isRegistered("c01", "c01"));
    }

    @AfterClass
    public static void deletingTestData() {
    }

    @Test
    public void registration() throws Exception {
        System.out.println("Run test registration");
        final Client client = new Client(8190);
        final Message msg = new AuthMessage(String.valueOf(Math.random()), "aaa",
                "aaa");
        client.write(msg);
        final Message serverMessage = client.read();
        final AuthMessage auth = (AuthMessage) serverMessage;
        assertEquals("aaa", auth.getUserLogin());
    }

    //@Ignore
    @Test
    public void repeatedLoginAfterBreakConnection() throws IOException {
        System.out.println("Run test repeatedLoginAfterBreakConnection");
        final Client c1 = new Client(8190);
        final Message msg = new AuthMessage(String.valueOf(Math.random()), "repeatedLoginAfterBreakConnection",
                "repeatedLoginAfterBreakConnection");
        c1.write(msg);
        c1.read();
        c1.socket.close();
        final Client c2 = new Client(8190);
        c2.write(msg);
        final Message serverMessage = c2.read();
        final AuthMessage auth = (AuthMessage) serverMessage;
        assertEquals("repeatedLoginAfterBreakConnection", auth.getUserLogin());
    }

    @Test
    public void failedDuplicateLogin() throws IOException {
        final Client client = new Client(8190);
        System.out.println("Run test failedDuplicateLogin");
        final Message msg = new AuthMessage(String.valueOf(Math.random()), "failedDuplicateLogin",
                "failedDuplicateLogin");
        client.write(msg);
        client.read();
        final Client cl2 = new Client(8190);
        cl2.write(msg);
        final Message answer = cl2.read();
        final AuthMessage auth = (AuthMessage) answer;
        assertNull(auth.getUserId());
    }


    @Test
    public void sendingBroadcastMessage() throws IOException {
        System.out.println("Run test sendingBroadcastMessage");
        final Client c2 = new Client(8190);
        final Client c1 = new Client(8190);
        final Message msg1 = new AuthMessage(String.valueOf(Math.random()), "c1",
                "c1");
        c1.write(msg1);
        Message auth = c1.read();
        assertNotNull(((AuthMessage) auth).getUserId());
        final Message msg2 = new AuthMessage(String.valueOf(Math.random()), "c2",
                "c2");
        c2.write(msg2);
        auth = c2.read();
        assertNotNull(((AuthMessage) auth).getUserId());
        c1.write(new BroadcastMessage("BroadcastMessage form c1", "c1"));
        final Message message = c2.read();
        assertEquals("BroadcastMessage form c1", message.getText());
    }

    /**
     * Создаёт заданное количество клиентов и одного контрольного клиента, который читает сообщения от остальных клиентов
     */
    @Test
    public void simpleLoad() throws IOException {
        System.out.println("simpleLoad");
        clients = new ArrayList<>(100);
        for (int i = 1; i < 10; i++) {
            Client c = new Client(8190);
            final String str = String.valueOf(Math.random());
            c.setUsername(str);
            c.setPass(str);
            clients.add(c);
        }
        for (final Client c : clients) {
            final Message msg = new AuthMessage(String.valueOf(Math.random()), c.getUsername(),
                    c.getPass());
            c.write(msg);
            c.read();
        }
        final Client c1 = new Client(8190);
        final Message msg1 = new AuthMessage(String.valueOf(Math.random()), "c01",
                "c01");
        c1.write(msg1);
        c1.read();
        for (final Client c : clients) {
            System.out.println("Sending test messages");
            c.write(new BroadcastMessage("BroadcastMessage for c01", c.getUsername()));
            final Message m = c1.read();
            System.out.println(m);
            assertEquals("BroadcastMessage for c01", m.getText());
        }
        System.out.println("test end");
    }
}
