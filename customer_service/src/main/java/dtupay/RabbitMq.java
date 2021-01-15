/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */

package dtupay;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import io.cucumber.messages.internal.com.google.gson.Gson;
import models.Message;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeoutException;


public class RabbitMq implements IRabbitMq {
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private DeliverCallback deliverCallback;
    private CustomerService service;
    public String queue;


    public RabbitMq(String queue, CustomerService service) throws IOException, TimeoutException, InterruptedException {
        //If testing, do not create RabbitMQ
        if(System.getenv("ENVIRONMENT") == null)
            return;

        this.queue = queue;

        //Sleep on startup to allow rabbitMQ server to start
        Thread.sleep(10*1000);

        this.service = service;

        //Connect to RabbitMQ server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq"); //rabbitmq:5672

        //Create connection
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


        //Define callback logic
        deliverCallback = (consumerTag, delivery) -> {
            System.out.println("Pending requests during receive: " + service.pendingRequests.size());
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
    public void sendMessage(String queue, Message message) {
        try{
            channel.queueDeclare(queue, false, false, false, null);
            channel.basicPublish("", queue, null, new Gson().toJson(message).getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            System.out.println(e);
        }

        System.out.println("Pending requests during send: " + service.pendingRequests.size());
    }

    @Override
    public void processMessage(JsonObject jsonObject){
        System.out.println("Customer_service processing message: " + jsonObject);
        String event = jsonObject.get("event").toString().replaceAll("\"", "");

        switch (event){
            //Outgoing events
            case "demo":
                //Call service logic here
                service.demo(jsonObject);
                break;

            //Callback events
            case "requestTokens":
                System.out.println("requestTokens reply received. Pending requests: "
                        + service.pendingRequests.size());
                service.requestTokens(jsonObject);
                break;
        }
    }
}
