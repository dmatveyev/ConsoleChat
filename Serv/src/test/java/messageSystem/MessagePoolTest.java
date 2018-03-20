package messageSystem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Денис on 20.03.2018.
 */
public class MessagePoolTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void notifyManagers() {
        final MessagePool pool = MessagePool.getInstance();
        final List<SimpleManager> managers = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            managers.add(new SimpleManager());
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

}