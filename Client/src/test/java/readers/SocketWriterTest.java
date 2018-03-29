package readers;

import application.messageSystem.Message;
import application.messageSystem.MessageFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class SocketWriterTest {
    private Path file;
    @Before
    public void createFile() {
        try {
            file = Files.createFile(Paths.get("writer.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @After
    public void deleteTestData() {
        try {
            Files.deleteIfExists(file);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void update() throws Exception {
        Server server = new Server(file);
        Message message = MessageFactory.createBroadcastMessage("test",
                "test");
        SocketWriter w = new SocketWriter(new ObjectOutputStream(
                Files.newOutputStream(file)));
        w.update(message);
        Message serverMessage = server.read(new ObjectInputStream(Files.newInputStream(file)));
        assertEquals("test", serverMessage.getText());
    }

}