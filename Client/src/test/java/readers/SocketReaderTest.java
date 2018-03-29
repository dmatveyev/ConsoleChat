package readers;

import application.messageSystem.Message;
import application.messageSystem.MessageFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class SocketReaderTest {
    private Path file;

    @Before
    public void createFile() {
        try {
            file = Files.createFile(Paths.get("messages.txt"));
        } catch (final IOException e) {
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
    public void readBroadcastMessage() throws IOException, ClassNotFoundException {
        final Server server = new Server(file);
        final Message message = MessageFactory.createBroadcastMessage("test",
                "test");
        server.write(new ObjectOutputStream(
                Files.newOutputStream(file)), message);
        final SocketReader reader = new SocketReader(
                new ObjectInputStream(Files.newInputStream(file)));
        final Message newMessage = reader.read();
        assertEquals("test", newMessage.getText());
    }
}