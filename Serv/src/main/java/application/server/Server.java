package application.server;


import application.messageSystem.Manager;
import application.messageSystem.MessagePool;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import application.server.clientData.UserSessionManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * Класс сервера
 * Created by Денис on 06.03.2018.
 */
@Component
public class Server implements ApplicationContextAware {
    public static final Logger logger = Logger.getLogger("Server");
    private ServerSocket serverSocket;
    public int clientId = 1;
    private final int port;
    private ClientHandler clientHandler;
    public Socket clientSocket;
    private UserSessionManager sessionManager;
    private MessagePool messagePool;
    private Manager messageManager;
    private ApplicationContext applicationContext;

    public Server(@Value("8190") final int port) {
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler("system.log", 1000000, 5, true);
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (final IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        assert fileHandler != null;
        logger.addHandler(fileHandler);
        logger.setLevel(Level.INFO);
        fileHandler.setLevel(Level.INFO);
        this.port = port;
    }

    void start() {
        try {
            logger.info("Starting application.server");
            logger.info("Registering beans");
            serverSocket = new ServerSocket(port);
            messagePool.registerManager(messageManager);
            logger.log(Level.INFO, "{0}: Server started on port {1}",
                    new String[]{this.getClass().getSimpleName(), String.valueOf(port)});
            while (true) {
                clientSocket = serverSocket.accept();
                logger.log(Level.INFO, "Spawning " + clientId);
                clientHandler = applicationContext.getBean("clientHandler", ClientHandler.class);
                messageManager.addHandler(clientId, clientHandler);
                final Thread t = new Thread(clientHandler);
                t.start();
                clientId++;
            }
        } catch (final IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        } finally {
            stop();
        }
    }


    private void stop() {
        try {
            sessionManager.unactivatedAll();
            serverSocket.close();
        } catch (final IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }
    @Autowired
    public void setSessionManager(final UserSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    @Autowired
    public void setMessagePool(final MessagePool messagePool) {
        this.messagePool = messagePool;
    }
    @Autowired
    public void setMessageManager(final Manager messageManager) {
        this.messageManager = messageManager;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
