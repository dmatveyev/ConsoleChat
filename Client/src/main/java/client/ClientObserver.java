package client;

import messageSystem.Message;

public interface ClientObserver {
    void updateClient(Message message);
}