package server;

import org.junit.*;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by Денис on 08.03.2018.
 */
public class ClientHandlerTest {

    Client client;
    String message;
    Thread serverThread;
   @BeforeClass
    public static void runServer () {
       Thread serverThread = new Thread(() -> new Server(8190));
       serverThread.start();
   }

    @Before
    public void setUp() throws Exception {
        client = new Client(8190);
        message = "testMessage";
    }

    @After
    public void tearDown() throws Exception {
        serverThread = null;
        client.socket.close();
        client=null;
        message = null;
    }

    @Test
    public void registration() throws Exception {
        client.read();
        client.write("aaa");
        client.read();
        client.write("aaa");
        String s = client.read();
        assertEquals("Hello, aaa!!!",s);
    }
    @Test
    public void repeatedLoginAfterBreakConnection() throws IOException {
        client.read();
        client.write("repeatedLoginAfterBreakConnection");
        client.read();
        client.write("repeatedLoginAfterBreakConnection");
        client.read();
        client.socket.close();
        client = new Client(8190);
        client.read();
        client.write("repeatedLoginAfterBreakConnection");
        client.read();
        client.write("repeatedLoginAfterBreakConnection");
        String s = client.read();
        assertEquals("Hello, repeatedLoginAfterBreakConnection!!!",s);
    }
    @Test
    public void repeatedLoginAfterUserExit() throws IOException {
        client.read();
        client.write("repeatedLoginAfterUserExit");
        client.read();
        client.write("repeatedLoginAfterUserExit");
        client.read();
        client.write("exit");
        client = new Client(8190);
        client.read();
        client.write("repeatedLoginAfterUserExit");
        client.read();
        client.write("repeatedLoginAfterUserExit");
        String s = client.read();
        assertEquals("Hello, repeatedLoginAfterUserExit!!!",s);
    }
    @Test
    public void failedDuplicateLogin () throws IOException {
        client.read();
        client.write("failedDuplicateLogin");
        client.read();
        client.write("failedDuplicateLogin");
        client.read();
        client = new Client(8190);
        client.read();
        client.write("failedDuplicateLogin");
        client.read();
        client.write("failedDuplicateLogin");
        String s2 = client.read();
        assertNotEquals("Hello, failedDuplicateLogin!!!",s2);
    }
   /* @Test
    public void ignoreBackspaseInLogin (){

    }*/
}
class Client {
    Socket socket;
    Scanner in;
    PrintWriter out;
    public Client (int port) throws IOException {
        socket = new Socket("localhost", port);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream())
                , true);
    }
    public String read (){
        String str="";
        try {
            if (in.hasNextLine())
                str = in.nextLine();
        } catch (Exception e) {
        e.printStackTrace();
    }
        return str;
    }

    public void write (String str) {
        try  {
            out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}