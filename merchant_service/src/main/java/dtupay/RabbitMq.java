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
import java.io.StringReader;
import java.nio.charset.StandardCharsets;


public class RabbitMq implements IRabbitMq {
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private DeliverCallback deliverCallback;
    private MerchantService service;


    public RabbitMq(String queue, MerchantService service) {
        //If testing, do not create RabbitMQ
        if(System.getenv("ENVIRONMENT") == null)
            return;

        //Sleep on startup to allow rabbitMQ server to start
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.service = service;

        //Connect to RabbitMQ server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq"); //rabbitmq:5672

        //Create connection
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(queue, false, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Define callback logic
        deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Token_service consumed a message");
            this.parseMessage(message);
        };

        //Subscribe to queue
        try {
            channel.basicConsume(queue, true, deliverCallback, consumerTag -> { });
        } catch (Exception e) {
            e.printStackTrace();
        }

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
