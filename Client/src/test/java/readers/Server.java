package readers;

import messageSystem.Message;

import java.io.*;
import java.nio.file.Files;


public class Server {
    private  ObjectInput input = null;
    private  ObjectOutput output = null;
    public final File file;


    public Server (final File file) throws IOException {
        this.file = file;
        try {
            input = new ObjectInputStream(Files.newInputStream(
                    file.toPath()));
            output = new ObjectOutputStream(
                    Files.newOutputStream(file.toPath())
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Message read(){
        try {
            return (Message) input.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.getCause();
        }
        return null;
    }

    public void write(final Message message){
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}