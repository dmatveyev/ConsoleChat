package server.databaseConnect;

import org.springframework.stereotype.Service;
import server.clientData.Session;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static server.Server.logger;

@Service("sessionDao")
public class SessionDAO implements DAO<Session> {

    private final ConnectDB connectDB;
    private final Properties sqlQueries;

    public SessionDAO() {
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
    public Session get(final String id) {
        Session session = null;
        try (Connection conn = connectDB.getConnection();
             PreparedStatement st = conn.prepareStatement(sqlQueries.getProperty("getUserSession"))) {
            st.setString(1, id);
            try(ResultSet result = st.executeQuery()) {
                if (result.next())
                    session = new Session(result.getString(1),
                            result.getString(2));
            }
        } catch (final SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return session;
    }

    @Override
    public void insert(final Session t) {
        try (Connection conn = connectDB.getConnection();
             PreparedStatement session = conn.prepareStatement(sqlQueries.getProperty("insertUserSession"))) {
            session.setString(1, t.getUserId());
            session.setString(2, t.getName());
            session.executeUpdate();
        } catch (final SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    @Override
    public void update(final Session t) {
        try (Connection conn = connectDB.getConnection();
             PreparedStatement st = conn.prepareStatement(sqlQueries.getProperty("updateSession"))) {
            st.setString(2, t.getUserId());
            st.setString(1, t.getName());
            st.executeUpdate();
        } catch (final SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    public void clearAllSessions() {
        try (Connection conn = connectDB.getConnection();
             PreparedStatement st = conn.prepareStatement(sqlQueries.getProperty("unactivatedAll"))) {
            st.execute();
        }catch (final SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }


    @Override
    public void delete(final String userId) {
        try (Connection conn = connectDB.getConnection();
             PreparedStatement st = conn.prepareStatement(sqlQueries.getProperty("deleteUserSession"))) {
            st.setString(1, userId);
            st.executeUpdate();
        } catch (final SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }
}