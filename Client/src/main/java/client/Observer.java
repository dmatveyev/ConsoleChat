package client;

import client.message.Message;

public interface Observer {
    void update(Message message);
}