package server.databaseConnect;

import com.microsoft.sqlserver.jdbc.SQLServerConnectionPoolDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.h2.jdbcx.JdbcDataSource;


import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private SQLServerDataSource dataSource;


    public ConnectDB() {
        dataSource = new SQLServerConnectionPoolDataSource();
        properties = new Properties();

        /*try {

            System.out.println(path);
            InputStream in = Files.newInputStream(Paths.get("Serv\\src\\main\\resources\\general.properties"));
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataSource.setURL(properties.getProperty("jdbc.url"));
        dataSource.setUser(properties.getProperty("jdbc.username"));
        dataSource.setPassword(properties.getProperty("jdbc.password"));*/

    }

    public Connection getConnection() throws SQLException {
        return getH2Connection();
    }

    public Connection getSQLServerConnection() throws SQLServerException {
        dataSource.setURL("jdbc:sqlserver://localhost:1433;databaseName=dendb");
        dataSource.setUser("sa");
        dataSource.setPassword("magenta");
        return dataSource.getConnection();
    }


    public  Connection getH2Connection() {
        try {
            String url = "jdbc:h2:./h2db";
            String name = "sa";
            String pass = "";

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