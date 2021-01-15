package brokers;

import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

public interface IMessageBroker {
    void sendMessage(String service, String message) throws IOException;
    void onQueue(String queue, DeliverCallback callback);
}
