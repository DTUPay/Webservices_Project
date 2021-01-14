/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */

package dtupay;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;


public class RabbitMq implements IRabbitMq {
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private DeliverCallback deliverCallback;
    private CustomerService service;


    public RabbitMq(String queue, CustomerService service) throws IOException, TimeoutException {
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
            String message = new String(delivery.getBody(), "UTF-8");
            this.parseMessage(message);
        };

        //Subscribe to queue
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> { });

    }

    @Override
    public void parseMessage(String message) {
        JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
        processMessage(jsonObject);
    }

    @Override
    public void sendMessage(String queue, JsonObject message) {
        try{
            channel.queueDeclare(queue, false, false, false, null);
            channel.basicPublish("", queue, null, message.toString().getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void processMessage(JsonObject jsonObject){
        String event = jsonObject.get("event").toString().replaceAll("\"", "");

        switch (event){
            case "demo":
                //Call service logic here
                service.demo(jsonObject);
                break;
        }
    }
}
