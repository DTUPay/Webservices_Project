package brokers;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import dto.*;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtupay.MessageRepository;
import dtupay.PaymentRepository;
import dtupay.PaymentService;
import dtupay.TokenRepository;
import models.Callback;
import models.Message;
import models.Payment;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
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
    PaymentService paymentService;
    MessageRepository messageRepository;
    TokenRepository tokenRepository;
    PaymentRepository paymentRepository;

    public PaymentBroker(PaymentService service) {
        this.paymentService = service;
        this.messageRepository = MessageRepository.getInstance();
        this.tokenRepository = TokenRepository.getInstance();
        this.paymentRepository = PaymentRepository.getInstance();

        try {

            factory.setHost("rabbitmq");

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
            e.printStackTrace();
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
            case "requestPayment":
                System.out.println("requestPayment event caught");
                requestPayment(message, payload);
                break;
            case "requestPaymentTokenUsed":
                System.out.println("requestPaymentTokenUsed event caught");
                requestPaymentTokenUsed(message, payload);
                break;
            case "requestPaymentCustomerFetched":
                System.out.println("requestPaymentCustomerFetched event caught");
                requestPaymentCustomerFetched(message, payload);
                break;
            case "getRefund":
                System.out.println("requestTokens event caught");
                getRefund(message, payload);
                break;
            case "refundPaymentTokenUsed":
                System.out.println("refundPaymentTokenUsed event caught ");
                getRefundTokenUsed(message, payload);
                break;
            case "useToken":
                System.out.println("useToken event caught");
                //useToken(message, payload);
                break;
            default:
                System.out.println("Event not handled: " + message.getEvent());
        }
    }

    private void requestPayment(Message message, JsonObject payload){
        PaymentDTO dto = gson.fromJson(payload.toString(), PaymentDTO.class);
        message.setPayload(dto);
        messageRepository.saveMessageObject(message);
        useToken(dto.getTokenID(), "requestPaymentTokenUsed", message.getRequestId());
    }

    private void requestPaymentTokenUsed(Message message, JsonObject payload){
        Message reply;
        Message originalMessage = messageRepository.getMessageObject(message.getRequestId());
        TokenDTO tokenDTO = null;
        try{
            tokenDTO = gson.fromJson(payload.toString(), TokenDTO.class);
        } catch (Exception e) {
            message.setStatus(400);
        }
        if(message.getStatus() != 200 || tokenDTO == null){
            messageRepository.removeMessageObject(message.getRequestId());
            reply = createReply(originalMessage);
            reply.setStatus(404); //TODO set correct error code
            reply.setStatusMessage("The token could not be validated");
            sendMessage(reply);
            return;
        }

        System.out.println("Original message: " + originalMessage.toString());
        tokenRepository.saveTokenObject(originalMessage.getRequestId(), tokenDTO);
        Message getCustomerMessage = new Message("customer_service", "getCustomerByID");
        Callback callback = new Callback("payment_service", "requestPaymentCustomerFetched");
        getCustomerMessage.setCallback(callback);
        getCustomerMessage.setRequestId(message.getRequestId());
        CustomerIDDTO customerId = new CustomerIDDTO();
        customerId.setCustomerID(tokenDTO.getCustomerID());
        getCustomerMessage.setPayload(customerId);
        sendMessage(getCustomerMessage);
    }

    private void requestPaymentCustomerFetched(Message message, JsonObject payload){
        Message reply;
        Message originalMessage = messageRepository.getMessageObject(message.getRequestId());
        messageRepository.removeMessageObject(message.getRequestId());
        TokenDTO tokenDTO = tokenRepository.getMessageObject(message.getRequestId());
        tokenRepository.removeMessageObject(message.getRequestId());
        System.out.println("original message: " + originalMessage.payload);

        CustomerDTO customerDTO = null;
        UUID paymentID;
        try{
            customerDTO = gson.fromJson(payload.toString(), CustomerDTO.class);
        } catch (Exception e) {
            message.setStatus(400);
        }
        if(message.getStatus() != 200 || tokenDTO == null){
            reply = createReply(originalMessage);
            reply.setStatus(404); //TODO set correct error code
            reply.setStatusMessage("Could not fetch customer account id");
            sendMessage(reply);
            return;
        }

        try{
            paymentID = paymentService.createPayment((PaymentDTO) originalMessage.payload, customerDTO, tokenDTO);
        } catch (BankServiceException_Exception e) {
            reply = createReply(originalMessage);
            reply.setStatus(400); //TODO set correct error code
            reply.setStatusMessage("Error while making payment: " + e.getMessage());
            sendMessage(reply);
            return;
        }
        reply = createReply(originalMessage);
        reply.payload = new PaymentIDDTO(paymentID);
        sendMessage(reply);

        //Report transaction to reporting service
        reportTransactionUpdate(paymentRepository.getPayment(paymentID));
    }

    private void getRefund(Message message, JsonObject payload){
        RefundDTO dto = gson.fromJson(payload.toString(), RefundDTO.class);
        message.setPayload(dto);
        messageRepository.saveMessageObject(message);
        useToken(dto.getTokenID(), "refundPaymentTokenUsed", message.getRequestId());
    }

    private void getRefundTokenUsed(Message message, JsonObject payload){
        Message reply;
        Message originalMessage = messageRepository.getMessageObject(message.getRequestId());
        messageRepository.removeMessageObject(message.getRequestId());
        RefundDTO refundDTO = (RefundDTO) originalMessage.getPayload();
        TokenDTO tokenDTO = null;
        try{
            tokenDTO = gson.fromJson(payload.toString(), TokenDTO.class);
        } catch (Exception e) {
            message.setStatus(400);
        }
        if(message.getStatus() != 200 || tokenDTO == null){
            reply = createReply(originalMessage);
            reply.setStatus(404); //TODO set correct error code
            reply.setStatusMessage("The token could not be validated");
            sendMessage(reply);
            return;
        }

        try{
            paymentService.refundPayment(refundDTO, tokenDTO);
        } catch (Exception e) {
            reply = createReply(originalMessage);
            reply.setStatus(404); //TODO set correct error code
            reply.setStatusMessage("Error while refunding payment: " + e.getMessage());
            sendMessage(reply);
            return;
        }

        reply = createReply(originalMessage);
        sendMessage(reply);

        //Send transaction update to reporting service
        reportTransactionUpdate(paymentRepository.getPayment(refundDTO.getPaymentID()));
    }

    public void useToken(UUID tokenID, String callbackEvent, UUID requestID){
        Message message = new Message();
        message.setEvent("useToken");
        message.setService("token_service");
        message.setRequestId(requestID);

        TokenIDDTO payload = new TokenIDDTO();
        payload.setTokenID(tokenID);
        message.setPayload(payload);
        message.setCallback(new Callback("payment_service", callbackEvent));
        sendMessage(message);
    }

    public void reportTransactionUpdate(Payment payment){
        Message message = new Message();
        String text = gson.toJson(payment);
        Payment paymentClone = gson.fromJson(text, Payment.class);

        paymentClone.setMerchantAccountID("redacted");
        paymentClone.setCustomerAccountID("redacted");
        message.setEvent("transactionUpdate");
        message.setService("reporting_service");
        message.setPayload(paymentClone);
        sendMessage(message);
    }


}
