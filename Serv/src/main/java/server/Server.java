package server;



import messageSystem.MessageFactory;
import messageSystem.MessageManager;
import messageSystem.MessagePool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by Денис on 06.03.2018.
 */
public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int clientId=1;
    private int port;
    public static Logger logger;
    private FileHandler fileHandler;

    public Server(int port)  {
        logger = Logger.getLogger("Server");
        try {
            fileHandler = new FileHandler("system.log",0,10,true);
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fileHandler);
        logger.setLevel(Level.FINEST);
        fileHandler.setLevel(Level.INFO);

        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            logger.log(Level.INFO, "{0}: Server started on port {1}",
                    new String[]{this.getClass().getSimpleName(),String.valueOf(port)} );
            MessageFactory messageFactory = new MessageFactory();
            MessagePool messagePool = MessagePool.getInstance();
            MessageManager messageManager = new MessageManager(messageFactory);
            messagePool.registerManager(messageManager);
            while (true) {
                clientSocket = serverSocket.accept();
                logger.log(Level.INFO,"Spawing " + clientId);
                ClientHandler clientHandler = new ClientHandler(clientId, clientSocket,messageFactory);
                messageManager.addHandler(clientId,clientHandler);
                Thread t = new Thread(clientHandler);
                t.start();
                clientId++;
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
