package application.messageSystem;

import application.server.ClientHandler;

public interface Manager {

    void DoMessage(final MessageEvent messageEvent);
    void sendMessageToAll(final Message msg);
    void addHandler(final int id, final ClientHandler clientHandler);
    void removeHandler(final int id);
}