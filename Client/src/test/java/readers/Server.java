package readers;

import messageSystem.Message;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


public class Server {




    public Server (final Path file) throws IOException {

    }
    public Message read(ObjectInput input){
        try {
            return (Message) input.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.getCause();
        }
        return null;
    }

    public void write(ObjectOutput output, final Message message){
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}