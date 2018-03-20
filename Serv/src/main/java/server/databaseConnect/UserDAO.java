package server.databaseConnect;

import server.clientData.User;

import java.io.IOException;
import java.sql.*;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.logging.Level;

import static server.Server.logger;


public class UserDAO implements DAO<User> {
    private final ConnectDB connectDB;
    private final Properties sqlQueries;


    public UserDAO() {
        connectDB = new ConnectDB();
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


    public String getUserId(final String login, final String password) {
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
    public void insert(final User t) {
        try (Connection conn = connectDB.getConnection();
             PreparedStatement statement = conn.prepareStatement(sqlQueries.getProperty("insertUser"));
             PreparedStatement session = conn.prepareStatement(sqlQueries.getProperty("insertUserSession"))) {
            statement.setString(1, t.getUserId());
            statement.setString(2, t.getLogin());
            statement.setString(3, t.getPassword());
            statement.executeUpdate();
            //переписать создание сессии с использованием sessionDAO

            session.setString(1, t.getUserId());
            session.setString(2, null);
            session.executeUpdate();
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
             PreparedStatement st = conn.prepareStatement(sqlQueries.getProperty("deleteUserSession"));
             PreparedStatement st2 = conn.prepareStatement(sqlQueries.getProperty("deleteUser"))) {
            st.setString(1, userId);
            st2.setString(1, userId);
            st.executeUpdate();
            st2.executeUpdate();
        } catch (final SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }
}