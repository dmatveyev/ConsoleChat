package server;


import messageSystem.MessageManager;
import messageSystem.MessagePool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import server.clientData.UserSessionManager;
import server.clientData.UsersManager;
import server.databaseConnect.SessionDAO;
import server.databaseConnect.UserDAO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static server.Main.ctx;


/**
 * Класс сервера
 * Created by Денис on 06.03.2018.
 */
@Service("server")
public class Server {
    public static final Logger logger = Logger.getLogger("Server");
    private ServerSocket serverSocket;
    public int clientId = 1;
    private final int port;
    private ClientHandler clientHandler;
    public Socket clientSocket;


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
            logger.info("Starting server");
            logger.info("Registering beans");
            serverSocket = new ServerSocket(port);
            final MessagePool messagePool = (MessagePool) ctx.getBean("messagePool");
            final MessageManager messageManager = (MessageManager) ctx.getBean("messageManager");
            final UsersManager usersManager = (UsersManager) ctx.getBean("userManager");
            final UserSessionManager sessionManager = (UserSessionManager) ctx.getBean("sessionManager");
            final UserDAO userDAO = (UserDAO) ctx.getBean("userDao");
            final SessionDAO sessionDAO = (SessionDAO) ctx.getBean("sessionDao");
            messagePool.registerManager(messageManager);
            logger.log(Level.INFO, "{0}: Server started on port {1}",
                    new String[]{this.getClass().getSimpleName(), String.valueOf(port)});
            while (true) {
                clientSocket = serverSocket.accept();
                logger.log(Level.INFO, "Spawning " + clientId);
                clientHandler = ctx.getBean("clientHandler", ClientHandler.class);
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
            serverSocket.close();
        } catch (final IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }
}
