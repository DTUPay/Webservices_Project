package brokers;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import dto.ReportRequestDTO;
import dtupay.PaymentRepository;
import dtupay.ReportingService;
import models.Message;
import models.Payment;
import models.Report;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

/**
 * @author Oliver O. Nielsen
 */
public class ReportingBroker implements IMessageBroker {
    ConnectionFactory factory = new ConnectionFactory();
    Connection connection;
    Channel channel;
    DeliverCallback deliverCallback;
    Gson gson = new Gson();
    String queue = "reporting_service";
    ReportingService reportingService;

    public ReportingBroker(ReportingService service) {
        this.reportingService = service;

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
            case "transactionUpdate":
                System.out.println("transactionUpdate event caught");
                transactionUpdate(message, payload);
                break;
            case "getManagerReport":
                System.out.println("getManagerReport event caught");
                getManagerReport(message, payload);
                break;
            case "getMerchantReport":
                System.out.println("getMerchantReport event caught");
                getMerchantReport(message, payload);
                break;
            case "getCustomerReport":
                System.out.println("getCustomerReport event caught");
                getCustomerReport(message, payload);
                break;
            default:
                System.out.println("Event not handled: " + message.getEvent());
        }
    }

    private void transactionUpdate(Message message, JsonObject payload) {
        Payment payment = gson.fromJson(payload.toString(), Payment.class);
        reportingService.transactionUpdate(payment);
    }

    private void getManagerReport(Message message, JsonObject payload){
        Message reply = createReply(message);
        Report report = reportingService.getManagerReport();
        reply.setPayload(report);
        sendMessage(reply);
    }

    private void getMerchantReport(Message message, JsonObject payload){
        Message reply = createReply(message);
        try{
            ReportRequestDTO reportRequestDTO = gson.fromJson(payload.toString(), ReportRequestDTO.class);
            Report report = reportingService.getMerchantReport(reportRequestDTO);
            reply.setPayload(report);
            sendMessage(reply);
        } catch (Exception e) {
            reply.setStatus(400); //TODO set correct errorcode
            reply.setStatusMessage("Could not cast DTO");
            sendMessage(reply);
            return;
        }
    }

    private void getCustomerReport(Message message, JsonObject payload){
        Message reply = createReply(message);
        try{
            ReportRequestDTO reportRequestDTO = gson.fromJson(payload.toString(), ReportRequestDTO.class);
            Report report = reportingService.getCustomerReport(reportRequestDTO);
            reply.setPayload(report);
            sendMessage(reply);
        } catch (Exception e) {
            reply.setStatus(400); //TODO set correct errorcode
            reply.setStatusMessage("Could not cast DTO");
            sendMessage(reply);
            return;
        }
    }
}
