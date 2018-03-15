package client;

import client.message.Message;

public interface ClientObserver {
    void updateClient(Message message);
}