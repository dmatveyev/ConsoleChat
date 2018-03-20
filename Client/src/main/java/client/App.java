package client;

/**
 * Created by Денис on 06.03.2018.
 */
public class App {
    public static void main(String... args) {
        final Client client = new Client(8190);
        client.start();
    }
}
