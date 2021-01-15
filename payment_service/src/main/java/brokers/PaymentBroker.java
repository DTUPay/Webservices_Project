/**
@author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */

package brokers;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import dtupay.PaymentService;
import models.Message;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @author Bjorn Wilting
 */

public class PaymentBroker implements IMessageBroker {
    private ConnectionFactory factory = new ConnectionFactory();
    private Connection connection;
    private Channel channel;
    private DeliverCallback deliverCallback;
    private Gson gson = new Gson();
    private final String queue = "payment_service";
    private PaymentService paymentService;


    public PaymentBroker(PaymentService paymentService) throws IOException, TimeoutException, InterruptedException {
        this.paymentService = paymentService;

        try {
            factory.setHost("rabbitmq");
            //If testing, do not create RabbitMQ
            if (System.getenv("ENVIRONMENT") == null) {
                int attempts = 0;
                while (true) {
                    try {
                        connection = factory.newConnection();
                        channel = connection.createChannel();
                        channel.queueDeclare(queue, false, false, false, null);
                        this.listenOnQueue(queue);

                        break;
                    } catch (Exception e) {
                        attempts++;
                        if (attempts > 10)
                            throw e;
                        System.out.println("Could not connect to RabbitMQ queue " + queue + ". Trying again.");
                        //Wait before 5 seconds before retry
                        Thread.sleep(5 * 1000);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listenOnQueue(String queue) {
        //Define callback logic
        deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject(); //TODO: Validate Message, if it is JSON object

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
            channel.basicConsume(queue, true, callback, consumerTag -> { });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message createReply(Message originalMessage) {
        Message reply = new Message(originalMessage.getCallback().getService(), originalMessage.getCallback().getEvent());
        reply.setRequestId(originalMessage.getRequestId());
        return reply;
    }

    @Override
    public void processMessage(Message message, JsonObject payload) {

        switch (message.getEvent()) {
            default:
                System.out.println("Event not handled: " + message.getEvent());
        }
    }
}
