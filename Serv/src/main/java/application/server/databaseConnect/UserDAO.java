package application.server.databaseConnect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import application.server.clientData.Session;
import application.server.clientData.User;

import java.io.IOException;
import java.sql.*;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.logging.Level;

import static application.server.Server.logger;

@Component
public class UserDAO implements DAO<User> {
    @Autowired
    private ConnectDB connectDB;
    private final Properties sqlQueries;
    @Autowired
    private DAO sessionDAO;

    public UserDAO() {
        sqlQueries = new Properties();
        try {
            sqlQueries.loadFromXML(ClassLoader.getSystemResourceAsStream("sql_queries.xml"));
        } catch (final InvalidPropertiesFormatException e) {
            e.getCause();
            logger.log(Level.WARNING, e.getMessage(), e);
        } catch (final IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    @Override
    public User get(final String id) {
        final User user = new User();
        try (Connection conn = connectDB.getConnection();
             PreparedStatement st = conn.prepareStatement(sqlQueries.getProperty("getUserById"))) {
            st.setString(1, id);
            try (ResultSet res = st.executeQuery()) {
                res.next();
                user.setUserId(res.getString(1));
                user.setLogin(res.getString(2));
                user.setPassword(res.getString(3));
            } catch (final SQLException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }

        } catch (final SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return user;
    }


    public String getId(final String login, final String password) {
        String userId = null;
        try (Connection conn = connectDB.getConnection();
             PreparedStatement st = conn.prepareStatement(sqlQueries.getProperty("getUserId"))) {
            st.setString(1, login);
            st.setString(2, password);
            try (ResultSet res = st.executeQuery()) {
                if (res.next()) {
                    userId = res.getString(1);
                }
            }
        } catch (final SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return userId;
    }

    @Override
    public void clearAllSessions() {

    }


    @Override
    public void insert(final User t) {
        try (Connection conn = connectDB.getConnection();
             PreparedStatement statement = conn.prepareStatement(sqlQueries.getProperty("insertUser"))
        ) {
            statement.setString(1, t.getUserId());
            statement.setString(2, t.getLogin());
            statement.setString(3, t.getPassword());
            statement.executeUpdate();

            sessionDAO.insert(new Session(t.getUserId(), null));
        } catch (final SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    @Override
    public void update(final User t) {
    }


    @Override
    public void delete(final String userId) {
        try (Connection conn = connectDB.getConnection();
             PreparedStatement st = conn.prepareStatement(sqlQueries.getProperty("deleteUser"))) {
            sessionDAO.delete(userId);
            st.setString(1, userId);
            st.executeUpdate();
        } catch (final SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }
}