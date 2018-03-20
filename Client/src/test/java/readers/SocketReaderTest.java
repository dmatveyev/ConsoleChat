package readers;

import messageSystem.Message;
import messageSystem.MessageFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.ObjectInputStream;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class SocketReaderTest {
    private File file;
    @Before
    public void createFile() {
        file = new File ("messages.txt");
    }
    @Ignore
    @Test
    public void run() throws Exception {
        SocketReader reader = new SocketReader(
                new ObjectInputStream(Files.newInputStream(file.toPath())));
        Server server = new Server(file);
        Message message = MessageFactory.createBroadcastMessage("test",
                "test");
         server.write(message);

    }

}