package server.databaseConnect;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.h2.jdbcx.JdbcDataSource;


import java.io.File;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Инкапсулирует логику подключения к базе данных
 */
public class ConnectDB {
    private Properties properties;



    public ConnectDB() {
        properties = new Properties();
        try {
            properties.load(ClassLoader.getSystemResourceAsStream("general.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        String driver =properties.getProperty("dbdriver");
        if(driver.equals("sqlserver"))
            return getSQLServerConnection();
        if(driver.equals("h2"))
            return getH2Connection();
        return null;
    }

    public Connection getSQLServerConnection() throws SQLException {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        String url = properties.getProperty("jdbc.url");
        String name = properties.getProperty("jdbc.username");
        String pass = properties.getProperty("jdbc.password");
        dataSource.setURL(url);
        dataSource.setUser(name);
        dataSource.setPassword(pass);
        return dataSource.getConnection();
    }


    public  Connection getH2Connection() {
        try {
            String url = properties.getProperty("h2.url");
            String name = properties.getProperty("h2.username");
            String pass = properties.getProperty("h2.password");

            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL(url);
            ds.setUser(name);
            ds.setPassword(pass);
            Connection connection = DriverManager.getConnection(url, name, pass);
            try (Statement st = connection.createStatement()){
                    st.executeUpdate("create table if not exists users(id varchar(255), login varchar(255) ,password varchar(255))");
                    st.executeUpdate("create table if not exists user_session(id varchar(255), session varchar(255))");
            }
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}