package application.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * a
 * Created by Денис on 06.03.2018.
 */
public class Main {

    public static void main(final String[] args) {
        final Logger logger = Logger.getLogger("Server");
        ClassLoader.getSystemResource("general.properties");
        final Properties property = new Properties();
        try {
            property.load(ClassLoader.getSystemResourceAsStream("general.properties"));
        } catch (final IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        final     ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:META-INF/app-context-annotation.xml");
        final Server srv = (Server) ctx.getBean("server");
        srv.start();
    }
}
