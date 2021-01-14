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
import javax.json.JsonObjectBuilder;
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
    public void sendMessage(String queue, JsonObject message, String callbackFunction) {
        if(callbackFunction != null){
            JsonObjectBuilder callbackJson = Json.createObjectBuilder()
                    .add("service", this.queue)
                    .add("function", callbackFunction);
            message = Json.createObjectBuilder(message).add("callback", callbackJson).build();
        }
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
