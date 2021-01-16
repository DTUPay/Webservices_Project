package brokers;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import dtupay.ManagementService;
import dtupay.RestResponseHandler;
import models.Message;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

public class ManagementBroker implements IMessageBroker {
    ConnectionFactory factory = new ConnectionFactory();
    Connection connection;
    Channel channel;
    DeliverCallback deliverCallback;
    Gson gson = new Gson();
    RestResponseHandler responseHandler;
    String queue = "management_service";

    ManagementService managementService;

    public ManagementBroker(ManagementService managementService) {
        this.managementService = managementService;
        RestResponseHandler responseHandler = RestResponseHandler.getInstance();

        factory.setHost("rabbitmq");

        try {
            if(System.getenv("ENVIRONMENT") != null){
                int attempts = 0;
                while (true){
                    try{

                        connection = factory.newConnection();
                        channel = connection.createChannel();
                        channel.queueDeclare(queue, false, false, false, null);

                        this.listenOnQueue(queue);

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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listenOnQueue(String queue){
        deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject(); // @TODO: Validate Message, if it is JSON object

            this.processMessage(gson.fromJson(jsonObject.toString(), Message.class), jsonObject.getJsonObject("payload"));
        };

        onQueue(queue, deliverCallback);

    }

    @Override
    public void sendMessage(Message message) {
        try {
            channel.basicPublish("", message.getService(), null, gson.toJson(message).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onQueue(String queue, DeliverCallback callback) {
        try {
            channel.basicConsume(queue, true, callback, consumerTag -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
    @Override
    public Message createReply(Message originalMessage) {
        Message reply = new Message(originalMessage.getCallback().getService(), originalMessage.getCallback().getEvent());
        reply.setRequestId(originalMessage.getRequestId());
        return reply;
    }

    //Fire and forget
    private void sendMessage(String queue, Message message) throws Exception {
        try{
            sendMessage(message);
        } catch(Exception e){
            throw new Exception(e);
        }
    }

    // Decodes payload and calls customerService
    private void processMessage(Message message, JsonObject payload) {
        switch(message.getEvent()) {
            case "registerCustomerResponse":
                this.managementService.registerCustomerResponse(message, payload);
                break;
            case "removeCustomerResponse":
                this.managementService.removeCustomerResponse(message);
                break;
            case "registerMerchantResponse":
                this.managementService.registerMerchantResponse(message, payload);
                break;
            case "removeMerchantResponse":
                this.managementService.removeMerchantResponse(message);
                break;
            default:
                System.out.println("Event not handled: " + message.getEvent());
        }

    }

    private void sendMessage(Message message, AsyncResponse response) throws Exception {
        responseHandler.saveRestResponseObject(response);

        try{
            this.sendMessage(message);
            System.out.println("Message sent");
        } catch(Exception e){
            throw new Exception(e);
        }
    }

}
