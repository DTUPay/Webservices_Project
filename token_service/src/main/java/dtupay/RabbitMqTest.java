/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */

package dtupay;
import com.rabbitmq.client.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;


public class RabbitMqTest implements IRabbitMq {
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private DeliverCallback deliverCallback;
    private TokenService service;



    public RabbitMqTest(String queue, TokenService service) throws IOException, TimeoutException {
        this.service = service;

        //Connect to RabbitMQ server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq"); //rabbitmq:5672

        //Create connection
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(queue, false, false, false, null);

        //Define callback logic
        deliverCallback = (consumerTag, delivery) -> {
            System.out.println("Message received by token_service");
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Message interpreted by token_device");
            this.parseMessage(message);
        };

        //Subscribe to queue
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> { });

    }

    @Override
    public void parseMessage(String message) {
        JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
        System.out.println("Message converted to JSON object by token_service");
        service.processMessage(jsonObject);
    }

    @Override
    public void sendMessage(String queue, JsonObject message) {
        try{
            System.out.println("Sending message to customer_service");
            channel.queueDeclare(queue, false, false, false, null);
            channel.basicPublish("", queue, null, message.toString().getBytes(StandardCharsets.UTF_8));
            System.out.println("Message to customer_service sent");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public String getTokens(JsonObject jsonObject){
        return "";
    }
}
