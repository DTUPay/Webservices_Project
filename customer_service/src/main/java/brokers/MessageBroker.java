package brokers;

import com.rabbitmq.client.DeliverCallback;
import models.Message;

import java.io.IOException;

public interface MessageBroker {
    void sendMessage(Message message) throws IOException;
    void onQueue(String queue, DeliverCallback callback);
}
