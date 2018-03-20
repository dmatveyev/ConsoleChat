package readers;

import messageSystem.Message;
import messageSystem.MessageFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class SocketWriterTest {
    private File file;
    @Before
    public void createFile() {
        file = new File ("messages.txt");
    }
    @Ignore
    @Test
    public void update() throws Exception {
        Server server = new Server(file);
        Message message = MessageFactory.createBroadcastMessage("test",
                "test");
        SocketWriter w = new SocketWriter(new ObjectOutputStream(
                Files.newOutputStream(file.toPath())));
    }

}