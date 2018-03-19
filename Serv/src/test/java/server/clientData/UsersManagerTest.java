package server.clientData;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.regex.Matcher;

import static org.junit.Assert.*;

/**
 * Created by Денис on 08.03.2018.
 */
public class UsersManagerTest {
    User test;
    UsersManager usersManager;

    @Before
    public void setUp() throws Exception {
        test = new User();
        test.setLogin("test");
        test.setPassword("test");
        test.setUserId(String.valueOf(Math.random()));
        usersManager = UsersManager.getInstance();
    }
    @After
    public void tearDown() throws Exception {
       usersManager.deleteUser(test.getUserId());
    }
    @AfterClass
    public static void deletingData(){
        UsersManager manager = UsersManager.getInstance();
        manager.deleteUser(manager.isRegistered("test","test"));
        manager.deleteUser(manager.isRegistered("a",
                "a"));
        manager.deleteUser(manager.isRegistered("OldUser",
                "OldUser"));
    }
    @Test
    public void registerUser() throws IOException {
        System.out.println("Run test registerUser" );
        assertEquals(test.getUserId(), usersManager.registerUser(test));
    }

    @Test
    public void isRegisteredPositive() throws Exception {
        System.out.println("Run test isRegisteredPositive" );
        assertNotNull(usersManager.isRegistered(test.getLogin(), test.getPassword()));
    }

    @Test
    public void isRegisteredNegative() throws Exception {
        System.out.println("Run test isRegisteredNegative" );
        assertNull(usersManager.isRegistered("a", "a"));
    }

    @Test
    public void getRegisteredUser() throws Exception {
        System.out.println("Run test getRegisteredUser" );
        usersManager.registerUser(test);
        assertEquals(test, usersManager.getRegisteredUser(test.getUserId()));

    }

    @Test
    public void authorizeNewUser() throws Exception {
        System.out.println("Run test authorizeNewUser" );
        assertEquals(test, usersManager.authorize("test", "test"));
    }

    @Test
    public void authorizeOldUser() throws Exception {
        System.out.println("Run test authorizeOldUser" );
        User oldUser = new User();
        oldUser.setUserId(String.valueOf(Math.random()));
        oldUser.setLogin("OldUser");
        oldUser.setPassword("OldUser");
        usersManager.registerUser(oldUser);
        assertEquals(oldUser, usersManager.authorize("OldUser", "OldUser"));
    }

}