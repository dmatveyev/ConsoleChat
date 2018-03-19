package server.clientData;


import server.databaseConnect.UserDAO;

import java.io.IOException;
import java.util.logging.Level;

import static server.Server.logger;

/**
 * Управляет пользователями
 * Синглтон
 */
public class UsersManager {

    private static UsersManager usersManager;
    private final UserDAO userDAO;
    private final UserSessionManager userSessionManager;


    private UsersManager() {
        userDAO = new UserDAO();
        userSessionManager = UserSessionManager.getInstance();
    }

    public static UsersManager getInstance() {
        if (usersManager == null) {
            usersManager = new UsersManager();
        }
        return usersManager;
    }

    String registerUser(final User user) {
        userDAO.insert(user);
        return user.getUserId();
    }

    /**
     * Проверяет совпадение пользователя в списке зарегистрированных
     *
     * @param login    логин пользователя
     * @param password пароль пользователя
     * @return Id пользователя, если такой пользователь был найден,
     * null если пользователь не найден
     */
    public String isRegistered(final String login, final String password) {
        return userDAO.getUserId(login, password);
    }

    User getRegisteredUser(final String id) {
        return userDAO.get(id);
    }

    public void deleteUser(final String id) {
        userDAO.delete(id);
    }

    /**
     * Проверяет введенные данные и авторизует пользователя.
     *
     * @param login    Предполагаемый логин пользователя
     * @param password предполагаемый пароль пользователя.
     * @return Зарегистрированный или новый пользователь
     * @throws IOException Пробоасывается в случае, если есть активная сессия пользователя.
     */
    public synchronized User authorize(final String login, final String password) throws IOException {
        final User user;
        final String userId = usersManager.isRegistered(login, password);
        if (userId != null) {
            user = usersManager.getRegisteredUser(userId);
            Session ss = userSessionManager.isActive(user);
            if (ss.getName() == null) {
                ss = UserSessionManager.createUserSession(user);
                userSessionManager.doActive(ss);
                return user;
            } else {
                logger.log(Level.WARNING, "{0} can\\'t authorize user {1}",
                        new Object[]{this.getClass().getSimpleName(), login});
                return null;
            }
        } else {
            user = new User();
            user.setLogin(login);
            user.setPassword(password);
            final String userid = String.valueOf(Math.random());
            user.setUserId(userid);
            registerUser(user);
            userSessionManager.doActive(UserSessionManager.createUserSession(user));
            return user;
        }
    }
}

