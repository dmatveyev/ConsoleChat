package server.clientData;

import org.junit.*;
import org.springframework.context.support.GenericXmlApplicationContext;
import server.databaseConnect.ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

/**
 * Created by Денис on 20.03.2018.
 */
public class UserSessionManagerTest {

    private static UserSessionManager manager;
    private static ConnectDB connect;
    private static User user;
    private static UsersManager usersManager;

    private static GenericXmlApplicationContext ctx;

    @BeforeClass
    public static void start() {
        ctx = new GenericXmlApplicationContext();
        ctx.load("classpath:META-INF/app-context-annotation.xml");
        ctx.refresh();
        user = new User();
        user.setUserId(String.valueOf(Math.random()));
        user.setLogin("login");
        user.setPassword("password");
        manager = (UserSessionManager) ctx.getBean("sessionManager");
        connect = new ConnectDB();
        usersManager = (UsersManager) ctx.getBean("userManager");
        usersManager.registerUser(user);
        manager.Unactivated(user);

    }

    @Before
    public void createTestData() {

    }

    @After
    public void deleteTestData() {

    }
    @AfterClass
    public static void stop() {
        
    }


    @Test
    public void createUserSession() throws Exception {
        Session session = UserSessionManager.createUserSession(user);
        assertEquals(user.getUserId(), session.getUserId());
    }


    @Test
    public void getUserSession() throws Exception {
        manager.doActive(user);
        Session session = manager.getSession(user);
        assertEquals(user.getUserId(), session.getUserId());
        assertNotNull(session.getName());
    }

    @Test
    public void doActive() throws Exception {
        manager.Unactivated(user);
        manager.doActive(user);
        Session newSession = manager.getSession(user);
        assertNotNull(newSession.getName());
    }

    @Test
    public void unactivated() throws Exception {
        manager.Unactivated(user);
        Session unactivated = manager.getSession(user);
        assertNull(unactivated.getName());
    }

}