package server.clientData;

import org.junit.*;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by Денис on 08.03.2018.
 */
public class UsersManagerTest {
    private static GenericXmlApplicationContext c;
    private User test;
    private static UsersManager usersManager;


    @BeforeClass
    public static void start() {
        c = new GenericXmlApplicationContext();
        c.load("classpath:META-INF/app-context-annotation.xml");
        c.refresh();
        usersManager = (UsersManager) c.getBean("userManager");
    }

    @Before
    public void setUp() throws Exception {
        test = new User();
        test.setLogin("test");
        test.setPassword("test");
        test.setUserId(String.valueOf(Math.random()));

    }
    @After
    public void tearDown() throws Exception {
       usersManager.deleteUser(test.getUserId());
    }
    @AfterClass
    public static void deletingData(){
        usersManager.deleteUser(usersManager.isRegistered("test","test"));
        usersManager.deleteUser(usersManager.isRegistered("a",
                "a"));
        usersManager.deleteUser(usersManager.isRegistered("OldUser",
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
    public void authorizeNewUser() throws IOException {
        System.out.println("Run test authorizeNewUser" );
        assertEquals(test, usersManager.authorize("test", "test"));
    }

    @Test
    public void authorizeOldUser() throws IOException {
        System.out.println("Run test authorizeOldUser" );
        final User oldUser = new User();
        oldUser.setUserId(String.valueOf(Math.random()));
        oldUser.setLogin("OldUser");
        oldUser.setPassword("OldUser");
        usersManager.registerUser(oldUser);
        assertEquals(oldUser, usersManager.authorize("OldUser", "OldUser"));
    }

}