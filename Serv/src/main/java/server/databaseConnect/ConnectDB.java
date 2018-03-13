package server.databaseConnect;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;

import java.sql.SQLException;
import java.util.Properties;

public class ConnectDB {
    private Properties properties;
    private SQLServerDataSource dataSource;

    public ConnectDB() {
        dataSource = new SQLServerDataSource();
        properties = new Properties();
        try {
            InputStream in = Files.newInputStream(Paths.get("Serv/src/main/resources/general.properties"));
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataSource.setURL(properties.getProperty("jdbc.url"));
        dataSource.setUser(properties.getProperty("jdbc.username"));
        dataSource.setPassword(properties.getProperty("jdbc.password"));
    }

    public Connection getConnection() throws SQLException {

        return dataSource.getConnection();
    }
}