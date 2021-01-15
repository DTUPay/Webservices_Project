package brokers;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import models.Message;
import models.Payload;

import java.nio.charset.StandardCharsets;

public class RabbitMQ implements MessageBroker {
    ConnectionFactory factory = new ConnectionFactory();
    Connection connection;
    Channel channel;
    DeliverCallback deliverCallback;
    Gson gson = new Gson();

    public RabbitMQ(String queue) {
        try {

            factory.setHost("rabbitmq");

            int attempts = 0;
            while (true){
                try{

                    connection = factory.newConnection();
                    channel = connection.createChannel();
                    channel.queueDeclare(queue, false, false, false, null);

                    break;
                }catch (Exception e){
                    attempts++;
                    if(attempts > 10)
                        throw e;
                    System.out.println("Could not connect to RabbitMQ queue " + queue + ". Trying again.");
                    //Sleep before retrying connection
                    Thread.sleep(5*1000);
                }
            }

        } catch(Exception e){
            e.printStackTrace();;
        }
    }

    @Override
    public void sendMessage(Message message) {
        try {
            channel.basicPublish("", message.getService(), null, gson.toJson(message).getBytes(StandardCharsets.UTF_8));
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

    @Override
    public Message createReply(Message originalMessage){
        Message reply = new Message(originalMessage.getCallback().getService(), originalMessage.getCallback().getEvent());
        reply.setRequestId(originalMessage.getRequestId());
        return reply;
    }

}
