package application.server.clientData;

import application.server.databaseConnect.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import application.server.databaseConnect.SessionDAO;

/**
 * Управляет сессиями пользователей
 * Created by Денис on 08.03.2018.
 */
@Service("sessionManager")
public class UserSessionManager {
    public final DAO sessionDAO;

    @Autowired
    private UserSessionManager(final DAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    static Session createUserSession(final User user) {
        final String session = user.getUserId().concat(user.getLogin()).concat(user.getPassword());
        return new Session(user.getUserId(), session);
    }

    /**
     * Получает объект сессии пользователя
     *
     * @param user пользователь для проверки
     * @return Объект сессии пользователя, если есть активный пользователь. Null, если нет активного пользователя.
     */
    public Session getSession(final User user) {
        return (Session) sessionDAO.get(user.getUserId());
    }

    /**
     * Активизирует сессия для покльзователя
     *
     * @param user Объект сессии
     */
    void doActive(final User user) {
        Session session = createUserSession(user);
        sessionDAO.update(session);
    }

    /**
     * Удаляет сессию для поользователя
     * @param user Пользователь, для которого необходимо удалить сессию
     */
    public void Unactivated(final User user) {
        Session session = getSession(user);
        session.setName(null);
        sessionDAO.update(session);
    }

    public void unactivatedAll() {
        sessionDAO.clearAllSessions();
    }
}