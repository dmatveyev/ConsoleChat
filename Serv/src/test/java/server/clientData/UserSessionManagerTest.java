package server.clientData;

import org.junit.*;
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
    private String sessionName = "testSession";

    @BeforeClass
    public static void create() {
        user = new User();
        user.setUserId("test");
        user.setLogin("login");
        user.setPassword("password");
        manager = UserSessionManager.getInstance();
        connect = new ConnectDB();

    }

    @Before
    public void createTestData() {
        try (Connection conn = connect.getConnection();
             PreparedStatement st = conn.prepareStatement(
                     "Insert into user_session (id, session) values (?,?) ")) {
            st.setString(1, user.getUserId());
            st.setString(2, sessionName);
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    public void deleteTestData() {
        try (Connection conn = connect.getConnection();
             PreparedStatement st = conn.prepareStatement(
                     "delete from user_session where id = ?")) {
            st.setString(1, user.getUserId());
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void createUserSession() throws Exception {
        Session session = UserSessionManager.createUserSession(user);
        assertEquals(user.getUserId(), session.getUserId());
    }


    @Test
    public void getUserSession() throws Exception {
        Session session = manager.getSession(user);
        assertEquals(user.getUserId(), session.getUserId());
        assertEquals(sessionName, session.getName());
    }

    @Test
    public void doActive() throws Exception {
        String newSessionName = "newSession";
        Session session = new Session(user.getUserId(), newSessionName);
        manager.doActive(session);
        Session newSession = manager.getSession(user);
        assertEquals(newSessionName, newSession.getName());
    }

    @Test
    public void unactivated() throws Exception {
        manager.Unactivated(user);
        Session unactivated = manager.getSession(user);
        assertNull(unactivated.getName());
    }

}