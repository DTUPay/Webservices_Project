package brokers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class RabbitMQ implements MessageBroker {
    ConnectionFactory factory = new ConnectionFactory();
    Connection connection;
    Channel channel;
    DeliverCallback deliverCallback;

    public RabbitMQ(String queue) {
        try {
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(queue, false, false, false, null);
        } catch(Exception e){
            e.printStackTrace();;
        }
    }

    @Override
    public void sendMessage(String service, String message) {
        try {
            channel.basicPublish("", service, null, message.toString().getBytes(StandardCharsets.UTF_8));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onQueue(String queue, DeliverCallback callback){
        try {
            channel.basicConsume(queue, true, callback, consumerTag -> { });
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
