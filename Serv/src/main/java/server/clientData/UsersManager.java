package server.clientData;

import server.databaseConnect.ConnectDB;
import server.databaseConnect.UserDAO;

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
    private UserDAO userDAO;
    private UserSessionManager userSessionManager;


    private UsersManager () {
        userDAO = new UserDAO();
        userSessionManager = UserSessionManager.getInstance();
        //createAdmin();
    }

    private void createAdmin() {
        User admin = new User();
        admin.setUserId("999");
        admin.setLogin("admin");
        admin.setPassword("password");
        userDAO.insert(admin);
    }

    public static UsersManager getInstance() {
        if (usersManager == null) {
            usersManager = new UsersManager();
        }
        return usersManager;
    }

    public String registerUser(User user)  {
         userDAO.insert(user);
        return user.getUserId();
    }

    /**
     * Проверяет совпадение пользователя в списке зарегистрированных
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return Id пользователя, если такой пользователь был найден,
     * null если пользователь не найден
     */
    public String isRegistered(String login, String password) {
        return userDAO.getUserId(login, password);
    }

    public User getRegisteredUser(String id) {
        return userDAO.get(id);
    }

    public void deleteUser(String id) {
       userDAO.delete(id);
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
        if (userId!= null) {
            user = usersManager.getRegisteredUser(userId);
            Session ss = userSessionManager.isActive(user);
            if (ss.getName() == null) {
                ss = userSessionManager.createUserSession(user);
                userSessionManager.doActive(ss);
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
            userSessionManager.doActive(userSessionManager.createUserSession(user));
            return user;
        }
    }
}

