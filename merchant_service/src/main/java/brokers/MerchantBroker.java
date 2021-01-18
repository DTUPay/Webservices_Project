package brokers;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import dto.MerchantDTO;
import dto.MerchantIDDTO;
import dto.PaymentDTO;
import dto.PaymentIDDTO;
import dtupay.MerchantService;
import dtupay.RestResponseHandler;
import exceptions.MerchantException;
import models.Callback;
import models.Merchant;
import models.Message;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author Mikkel Rosenfeldt Anderson & Benjamin
 */
public class MerchantBroker implements IMessageBroker {
    ConnectionFactory factory = new ConnectionFactory();
    Connection connection;
    Channel channel;
    DeliverCallback deliverCallback;
    Gson gson = new Gson();
    RestResponseHandler responsehandler;
    String queue = "merchant_service";
    MerchantService merchantService;

    public MerchantBroker(MerchantService service) {
        this.merchantService = service;
        this.responsehandler = RestResponseHandler.getInstance();

        try {

            factory.setHost("rabbitmq");

            //TODO change env to != null
            if(System.getenv("ENVIRONMENT") != null && System.getenv("CONTINUOUS_INTEGRATION") == null){
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

    // Decodes payload and calls customerService
    private void processMessage(Message message, JsonObject payload){

        switch(message.getEvent()) {
            case "registerMerchant":
                registerMerchant(message, payload);
                break;
            case "removeMerchant":
                removeMerchant(message, payload);
                break;
            case "getMerchant":
                getMerchant(message, payload);
                break;
            // generate report receivers
            case "returnMerchantSummary":
                returnMerchantReport(message, payload);
                break;
            case "requestPaymentComplete":
                requestPaymentComplete(message, payload);
                break;
            case "getMerchantById":
                getMerchantById(message, payload);
                break;
            default:
                System.out.println("Event not handled: " + message.getEvent());
        }
    }

    // @Status: Implemented
    public void registerMerchant(Message message, JsonObject payload){
        Message reply = createReply(message);
        try{
            Merchant merchant = gson.fromJson(payload.toString(), Merchant.class);
            merchantService.registerMerchant(merchant);

            MerchantDTO dto = new MerchantDTO();
            dto.setMerchantID(merchant.getMerchantID());
            dto.setName(merchant.getName());

            reply.setPayload(dto);
            reply.setStatus(201);

        } catch (MerchantException e) {
            reply.setStatus(400);
            reply.setStatusMessage(e.toString());
            sendMessage(reply);
            return;

        }
        sendMessage(reply);
    }

    // @Status: Implemented
    public void removeMerchant(Message message, JsonObject payload){
        Message reply = createReply(message);
        try{
            MerchantIDDTO dto = gson.fromJson(payload.toString(), MerchantIDDTO.class);
            System.out.println(dto.getMerchantID());
            merchantService.removeMerchant(dto.getMerchantID());
        } catch (MerchantException e) {
            reply.setStatus(400);
            reply.setStatusMessage(e.toString());
            sendMessage(reply);
            return;
        }
        sendMessage(reply);
    }

    public void getMerchant(Message message, JsonObject payload){
        Message reply = createReply(message);
        try{
            MerchantIDDTO dto = gson.fromJson(payload.toString(), MerchantIDDTO.class);
            reply.payload = merchantService.getMerchant(dto.getMerchantID());
        } catch (MerchantException e) {
            reply.setStatus(400);
            reply.setStatusMessage(e.toString());
            sendMessage(reply);
            return;
        }
        sendMessage(reply);
    }

    public void returnMerchantReport(Message message, JsonObject payload) {
        AsyncResponse request = responsehandler.getRestResponseObject(message.getRequestId());
        responsehandler.removeRestResponseObject(message.getRequestId());
        if(message.getStatus() != 200){
            request.resume(Response.status(message.getStatus()));
            return;
        }

        //TODO cast payload to expected DTO before returning
        request.resume(Response.status(message.getStatus()).entity(payload));
    }

    public void getMerchantById(Message message, JsonObject payload) {
        Message reply = createReply(message);
        try {
            MerchantIDDTO dto = gson.fromJson(payload.toString(), MerchantIDDTO.class);
            Merchant merchant = merchantService.getMerchant(dto.getMerchantID());

            reply.payload = merchant;
        } catch(MerchantException e){
            reply.setStatus(400);
            reply.setStatusMessage(e.toString());
            this.sendMessage(reply);
            return;
        }

        this.sendMessage(reply);
    }

    // @Status: Implemented
    // Request payment functions
    public void requestPayment(PaymentDTO payment, AsyncResponse response){
        try {
            Merchant merchant = this.merchantService.getMerchant(payment.getMerchantID());
            payment.setMerchantAccountID(merchant.getAccountNumber());
        } catch (MerchantException me) {
            response.resume(Response.status(400).entity(me.getMessage()).build());
            return;
        }

        Message message = new Message();
        message.setEvent("requestPayment");
        message.setService("payment_service");
        message.setCallback(new Callback("merchant_service", "requestPaymentComplete"));
        message.setPayload(payment);
        UUID requestId = responsehandler.saveRestResponseObject(response);
        message.setRequestId(requestId);

        this.sendMessage(message);
    }

    public void requestPaymentComplete(Message message, JsonObject payload) {
        //Return status of rabbitMQ request
        AsyncResponse request = responsehandler.getRestResponseObject(message.getRequestId());
        responsehandler.removeRestResponseObject(message.getRequestId());
        if(message.getStatus() != 200) {
            request.resume(Response.status(message.getStatus()).entity(message.getStatusMessage()).build());
            return;
        }
        request.resume(Response.status(message.getStatus()).entity(gson.fromJson(payload.toString(), PaymentIDDTO.class)).build());
    }
    
    /*
    Generate report functions
     */

    public void generateReport(PaymentDTO reportRequestDTO, AsyncResponse response){
        UUID requestId = responsehandler.saveRestResponseObject(response);

        Message message = new Message();
        message.setEvent("getMerchantSummary");
        message.setService("payment_service");
        message.setRequestId(requestId);

        message.setPayload(reportRequestDTO);
        message.setCallback(new Callback("merchant_service", "returnMerchantSummary"));
        sendMessage(message);
    }

    // Request payment functions
    public void requestPaymentResponse(Message message){
        AsyncResponse response = responsehandler.getRestResponseObject(message.getRequestId());
        response.resume(Response.status(message.getStatus()).entity(message.getStatusMessage()).build());
    }


}
