package messageSystem;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.GenericXmlApplicationContext;
import server.clientData.UserSessionManager;
import server.clientData.UsersManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**Тест для пула сообщений
 * Created by Денис on 20.03.2018.
 */
public class MessagePoolTest {

    private static GenericXmlApplicationContext ctx;
    private static MessagePool pool; 

    @BeforeClass
    public static void start() {
        ctx = new GenericXmlApplicationContext();
        ctx.load("classpath:META-INF/app-context-annotation.xml");
        ctx.refresh();
        pool = (MessagePool) ctx.getBean("messagePool");
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void notifyManagers() throws Exception {

        final List<SimpleManager> managers = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            managers.add(new SimpleManager( ctx.getBean("userManager", UsersManager.class),
                    ctx.getBean("sessionManager", UserSessionManager.class)));
        }
        for (SimpleManager manager : managers) {
            pool.registerManager(manager);
        }
        final MessagePair pair = new MessagePair(500,
                MessageFactory.createBroadcastMessage("text", "user"));
        pool.addMessage(pair);
        for (SimpleManager manager : managers) {
            assertEquals(pair.getMessage().toString(), manager.getMessage().getMessage().toString());
        }
    }

    @Test
    public void notifyVoid() {

        final MessagePair pair = new MessagePair(500,
                MessageFactory.createBroadcastMessage("text", "user"));
        try {
            pool.addMessage(pair);
            assertTrue(true);
        }catch (Exception e) {
            assertTrue(false);
        }
    }

}