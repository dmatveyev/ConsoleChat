package server.clientData;

import server.databaseConnect.ConnectDB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Синглтон
 */
public class UsersManager {

    private static UsersManager usersManager;
    private ConnectDB connectDB;

    private Map<String,User> users;
    private UserSessionManager userSessionManager;


    private UsersManager () {
        connectDB = new ConnectDB();
        userSessionManager = UserSessionManager.getInstance();
        User admin = createAdmin();
        users = new ConcurrentHashMap<>();
        users.put("0",admin);
    }

    private User createAdmin() {
        User admin = new User();
        admin.setLogin("admin");
        admin.setPassword("password");

        return admin;
    }

    public static UsersManager getInstance() {
        if (usersManager == null) {
            usersManager = new UsersManager();
        }
        return usersManager;
    }
    /*
    В этом методе добавить вставку пользователя в базу данных
     */
    public String registerUser(User user)  {
        String id = user.getUserId();
        try(Connection conn = connectDB.getConnection()) {
        PreparedStatement statement = conn.prepareStatement("insert into users (id,login, password) values (?,?,?)");
        statement.setString(1, id);
        statement.setString(2, user.getLogin());
        statement.setString(3, user.getPassword());
        statement.executeUpdate();
        users.put(id, user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * Проверяет совпадение пользователя в списке зарегистрированных
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return Id пользователя, если такой пользователь был найден
     */
    /*
    Этот метод должен считывать данные из базы.
     */
    public String isRegistered(String login, String password) {
        String userId = "";
        try (Connection conn = connectDB.getConnection()) {
            PreparedStatement st = conn.prepareStatement("select * from users where login = ? and password = ?");
            st.setString(1,login);
            st.setString(2, password);
            ResultSet res = st.executeQuery();
            if (res.next()) {
                userId = res.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }
    /*
    Этот метод должен обращаться к базе.
     */
    public User getRegisteredUser(String id) {
        User user = new User();
        try (Connection conn = connectDB.getConnection()) {
        PreparedStatement st = conn.prepareStatement("select * from users where id = ?");
        st.setString(1, id);
            ResultSet res = st.executeQuery();
            res.next();
            user.setUserId(res.getString(1));
            user.setLogin(res.getString(2));
            user.setPassword(res.getString(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User deleteUser(String id) {
        return users.remove(id);
    }

    /**
     * Проверяет введенные данные и авторизует пользователя.
     * @param login Предполагаемый логин пользователя
     * @param password предполагаемый пароль пользователя.
     * @return Зарегистрированный или новый пользователь
     * @throws IOException Пробоасывается в случае, если есть активная сессия пользователя.
     */
    public synchronized User authorize(String login, String password) throws IOException {
        User user;
        String userId = usersManager.isRegistered(login, password);
        if (userId!= "") {
            user = usersManager.getRegisteredUser(userId);
            if (userSessionManager.isActive(user) == ""|| userSessionManager.isActive(user) == null) {
                userSessionManager.doActive(userId,
                        userSessionManager.createUserSession(user));
                return user;
            } else {
                throw new IOException("User " +user.getLogin()  + " already authorized");
            }
        }else {
            user = new User ();
            user.setLogin(login);
            user.setPassword(password);
            String userid = String.valueOf(Math.random());
            user.setUserId(userid);
            registerUser(user);

            userSessionManager.doActive(user.getUserId(),
                    userSessionManager.createUserSession(user));
            return user;
        }
    }
}