package server.clientData;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.databaseConnect.UserDAO;

import java.io.IOException;
import java.util.logging.Level;

import static server.Server.logger;


/**
 * Управляет пользователями
 * Синглтон
 */
@Service("userManager")
public class UsersManager {

    private static UsersManager usersManager;
    private final UserDAO userDAO;
    private final UserSessionManager userSessionManager;

    @Autowired
    public UsersManager(final UserDAO userDAO, final UserSessionManager userSessionManager) {
        this.userDAO = userDAO;
        this.userSessionManager = userSessionManager;
    }

    public static UsersManager getInstance() {
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
        final String userId = isRegistered(login, password);
        if (userId != null) {
            user = getRegisteredUser(userId);
            Session ss = userSessionManager.getSession(user);
            if (ss.getName() == null) {
                ss = UserSessionManager.createUserSession(user);
                userSessionManager.doActive(user);
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
            final String id = String.valueOf(Math.random());
            user.setUserId(id);
            registerUser(user);
            userSessionManager.doActive(user);
            return user;
        }
    }
}

