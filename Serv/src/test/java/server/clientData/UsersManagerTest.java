package server.clientData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

    @Test
    public void registerUser(){
        assertEquals(test.getUserId(), usersManager.registerUser(test));
    }

    @Test
    public void isRegisteredPositive() throws Exception {
        assertNotNull(usersManager.isRegistered(test.getLogin(), test.getPassword()));
    }

    @Test
    public void isRegisteredNegative() throws Exception {
        assertNull(usersManager.isRegistered("a", "a"));
    }

    @Test
    public void getRegisteredUser() throws Exception {
        usersManager.registerUser(test);
        assertEquals(test, usersManager.getRegisteredUser(test.getUserId()));

    }

    @Test
    public void authorizeNewUser() throws Exception {
        assertEquals(test, usersManager.authorize("test", "test"));
    }

    @Test
    public void authorizeOldUser() throws Exception {
        User oldUser = new User();
        oldUser.setUserId(String.valueOf(Math.random()));
        oldUser.setLogin("OldUser");
        oldUser.setPassword("OldUser");
        usersManager.registerUser(oldUser);
        assertEquals(oldUser, usersManager.authorize("OldUser", "OldUser"));
    }

}