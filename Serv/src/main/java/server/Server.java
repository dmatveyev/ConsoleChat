package server;


import messageSystem.MessageManager;
import messageSystem.MessagePool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * Created by Денис on 06.03.2018.
 */
public class Server {
    public static Logger logger = Logger.getLogger("Server");

    private ServerSocket serverSocket;
    private int clientId = 1;
    private int port;



    public Server(final int port) {
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
            serverSocket = new ServerSocket(port);
            logger.log(Level.INFO, "{0}: Server started on port {1}",
                    new String[]{this.getClass().getSimpleName(), String.valueOf(port)});
            final MessagePool messagePool = MessagePool.getInstance();
            final MessageManager messageManager = new MessageManager();
            messagePool.registerManager(messageManager);

            while (true) {
                final Socket clientSocket = serverSocket.accept();
                logger.log(Level.INFO, "Spawning " + clientId);
                final ClientHandler clientHandler = new ClientHandler(clientId, clientSocket);
                messageManager.addHandler(clientId, clientHandler);
                final Thread t = new Thread(clientHandler);
                t.start();
                clientId++;
            }
        } catch (final IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }


    public void stop() {
        try {
            serverSocket.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
