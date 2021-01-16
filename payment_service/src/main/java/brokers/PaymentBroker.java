package brokers;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import dtupay.PaymentService;
import dtupay.RestResponseHandler;
import models.Message;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

/**
 * @author Mikkel Rosenfeldt Anderson & Oliver O. Nielsen
 */
public class PaymentBroker implements IMessageBroker {
    ConnectionFactory factory = new ConnectionFactory();
    Connection connection;
    Channel channel;
    DeliverCallback deliverCallback;
    Gson gson = new Gson();
    String queue = "payment_service";
    RestResponseHandler responseHandler;
    PaymentService paymentService;

    public PaymentBroker(PaymentService service) {
        this.paymentService = service;
        this.responseHandler = RestResponseHandler.getInstance();

        try {

            factory.setHost("rabbitmq");

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

        } catch(Exception e){
            e.printStackTrace();;
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

    // Decodes payload and calls customerService
    private void processMessage(Message message, JsonObject payload){

        switch(message.getEvent()) {
            case "requestTokens":
                System.out.println("requestTokens event caught");
                //requestTokens(message, payload);
                break;
            case "useToken":
                System.out.println("useToken event caught");
                //useToken(message, payload);
                break;
            default:
                System.out.println("Event not handled: " + message.getEvent());
        }

    }


    @Override
    public void sendMessage(Message message) {
        try {
            channel.queueDeclare(message.getService(), false, false, false, null);
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
