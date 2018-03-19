package server.databaseConnect;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.h2.jdbcx.JdbcDataSource;


import java.io.IOException;
import java.sql.Connection;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static server.Server.logger;


/**
 * Инкапсулирует логику подключения к базе данных
 */

class ConnectDB {


    private final Properties properties;

    ConnectDB() {
        properties = new Properties();
        try {
            properties.load(ClassLoader.getSystemResourceAsStream("general.properties"));
        } catch (final IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    Connection getConnection() throws SQLException {
        final String driver = properties.getProperty("dbdriver");
        if (driver.equals("sqlserver"))
            return getSQLServerConnection();
        if (driver.equals("h2"))
            return getH2Connection();
        return null;
    }

    private Connection getSQLServerConnection() {
        final SQLServerDataSource dataSource = new SQLServerDataSource();
        final String url = properties.getProperty("jdbc.url");
        final String name = properties.getProperty("jdbc.username");
        final String pass = properties.getProperty("jdbc.password");
        dataSource.setURL(url);
        dataSource.setUser(name);
        dataSource.setPassword(pass);
        try {
            return dataSource.getConnection();
        } catch (final SQLServerException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return null;
    }

    private Connection getH2Connection() {
        try {
            final String url = properties.getProperty("h2.url");
            final String name = properties.getProperty("h2.username");
            final String pass = properties.getProperty("h2.password");

            final JdbcDataSource ds = new JdbcDataSource();
            ds.setURL(url);
            ds.setUser(name);
            ds.setPassword(pass);
            final Connection connection = ds.getConnection();
            try (Statement st = connection.createStatement()) {
                st.executeUpdate("create table if not exists users(id varchar(255), login varchar(255) ,password varchar(255))");
                st.executeUpdate("create table if not exists user_session(id varchar(255), session varchar(255))");
            } catch (final SQLException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            return connection;
        } catch (final SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return null;
    }
}