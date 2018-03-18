package server;

import javax.sql.DataSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

/**     a
 * Created by Денис on 06.03.2018.
 */
public class Main {
    public static void main(String... args) {
        Properties property = new Properties();
        try {
            property.load(Files.newInputStream(Paths.get("Serv\\src\\main\\resources\\general.properties"),
                    StandardOpenOption.READ));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Server srv = new Server(Integer.parseInt(property.getProperty("port")));
        srv.start();
        
    }
}
