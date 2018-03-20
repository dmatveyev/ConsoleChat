package readers;

import messageSystem.Message;
import messageSystem.MessageFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
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
            file = Files.createFile(Paths.get("messages.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Ignore
    @Test
    public void update() throws Exception {
        Server server = new Server(file);
        Message message = MessageFactory.createBroadcastMessage("test",
                "test");
        SocketWriter w = new SocketWriter(new ObjectOutputStream(
                Files.newOutputStream(file)));
    }

}