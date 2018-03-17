package server;

import messageSystem.AuthMessage;
import messageSystem.BroadcastMessage;
import messageSystem.Message;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import server.clientData.UsersManager;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Денис on 17.03.2018.
 */
public class SendingMessagesTest {
    @BeforeClass
    public static void runServer () {
        Thread serverThread = new Thread(() -> new Server(8190));
        serverThread.start();
    }

    @AfterClass
    public  static void deletingTestData() {

        UsersManager manager = UsersManager.getInstance();
        manager.deleteUser(manager.isRegistered("c1","c1"));
        manager.deleteUser(manager.isRegistered("c2",
                "c2"));

    }
    @Test
    public void sendingBroadcastMessage() throws IOException {
        System.out.println("Run test sendingBroadcastMessage");
        Client c2 = new Client(8190);
        Client c1 = new Client(8190);
        Message msg1 = new AuthMessage(String.valueOf(Math.random()), "c1",
                "c1");
        c1.write(msg1);
        c1.read();
        Message msg2 = new AuthMessage(String.valueOf(Math.random()), "c2",
                "c2");
        c2.write(msg2);
        Message auth = c2.read();
        c1.write(new BroadcastMessage("BroadcastMessage", "c1"));
        Message message = c2.read();

        BroadcastMessage b = (BroadcastMessage)message;
        System.out.println(b);
        assertEquals("BroadcastMessage", b.getText());
    }
}
