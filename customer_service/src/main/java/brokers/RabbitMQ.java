package brokers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import models.Message;

import java.nio.charset.StandardCharsets;

public class RabbitMQ implements MessageBroker {
    ConnectionFactory factory = new ConnectionFactory();
    Connection connection;
    Channel channel;
    DeliverCallback deliverCallback;
    Gson gson = new Gson();

    public RabbitMQ(String queue) {
        try {
            if(System.getenv("ENVIRONMENT") == null){
                return;
            }
            Thread.sleep(10*1000);
            factory.setHost("rabbitmq");
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(queue, false, false, false, null);
        } catch(Exception e){
            e.printStackTrace();;
        }
    }

    @Override
    public void sendMessage(Message message) {
        try {
            System.out.println("Sending message");
            channel.basicPublish("", message.getService(), null, gson.toJson(message).getBytes(StandardCharsets.UTF_8));
        } catch(Exception e) {
            System.out.println("Send error");
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
