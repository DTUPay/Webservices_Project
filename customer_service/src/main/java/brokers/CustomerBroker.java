package brokers;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import dto.*;
import dtupay.CustomerService;
import dtupay.RestResponseHandler;
import exceptions.CustomerException;
import models.Callback;
import models.Customer;
import models.Message;
import models.Report;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * @author Mikkel & Benjamin & Rubatharisan & Oliver O. Nielsen
 */

public class CustomerBroker implements IMessageBroker {
    ConnectionFactory factory = new ConnectionFactory();
    Connection connection;
    Channel channel;
    DeliverCallback deliverCallback;
    Gson gson = new Gson();
    RestResponseHandler responseHandler;
    String queue = "customer_service";

    CustomerService customerService;

    public CustomerBroker(CustomerService customerService) {
        this.customerService = customerService;
        this.responseHandler = RestResponseHandler.getInstance();

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
            e.printStackTrace();;
        }
    }

    //track 'customer_service'-queue
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
            channel.queueDeclare(message.getService(), false, false, false, null);
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
            case "registerCustomer":
                registerCustomer(message, payload);
                break;
            case "removeCustomer":
                removeCustomer(message, payload);
                break;
            case "requestTokensResponse":
                requestTokensResponse(message, payload);
                break;
            case "getCustomerByID":
                getCustomerById(message, payload);
                break;
            case "receiveReport":
                receiveReport(message, payload);
                break;
            case "requestRefundResponse":
                requestRefundResponse(message, payload);
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


    /*
        Customer Specific Functions
     */

    // @Status: implemented
    public void registerCustomer(Message message, JsonObject payload) {
        Message reply = this.createReply(message);

        try {
            CustomerDTO dto = gson.fromJson(payload.toString(), CustomerDTO.class);

            Customer customer = new Customer();
            customer.setFirstName(dto.getFirstName());
            customer.setLastName(dto.getLastName());
            customer.setAccountID(dto.getAccountNumber());

            UUID customerID = customerService.registerCustomer(customer);
            dto.setCustomerID(customerID);
            reply.setPayload(dto);
            reply.setStatus(201);

        } catch(CustomerException e){
            reply.setStatus(400);
            reply.setStatusMessage(e.toString());
            this.sendMessage(reply);
            return;
        }

        this.sendMessage(reply);
    }

    // @Status: implemented
    public void removeCustomer(Message message, JsonObject payload) {
        Message reply = this.createReply(message);
        CustomerDTO customer = gson.fromJson(payload.toString(), CustomerDTO.class);

        try {
            customerService.removeCustomer(customer.getCustomerID());
        } catch(CustomerException e){
            reply.setStatus(400);
            reply.setStatusMessage(e.toString());
            this.sendMessage(reply);
            return;
        }

        this.sendMessage(reply);

    }


    public void getCustomerById(Message message, JsonObject payload){
        Message reply = createReply(message);
        try {
            CustomerIDDTO dto = gson.fromJson(payload.toString(), CustomerIDDTO.class);
            Customer customer = customerService.getCustomer(dto.getCustomerID());
            CustomerDTO customerDTO = new CustomerDTO(customer);

            reply.payload = customerDTO;
        } catch(CustomerException e){
            reply.setStatus(400);
            reply.setStatusMessage(e.toString());
            this.sendMessage(reply);
            return;
        }

        this.sendMessage(reply);
    }



    /*
        Handle REST Async calls
     */

    // @TODO: Missing in UML
    // @Status: Implemented
    public void requestRefund(RefundDTO refund, AsyncResponse response){

        Message message = new Message();
        message.setEvent("getRefund");
        message.setService("payment_service");
        message.setPayload(refund);
        message.setCallback(new Callback("customer_service", "requestRefundResponse"));

        UUID requestId = responseHandler.saveRestResponseObject(response);
        message.setRequestId(requestId);

        this.sendMessage(message);
    }

    // @TODO: Missing in UML
    // @Status: implemented
    public void requestTokens(TokensDTO token, AsyncResponse response) {

        try {
            customerService.canRequestTokens(token.getCustomerID(), token.getAmount());
        } catch (CustomerException ce) {
            response.resume(Response.status(400).entity(ce.getMessage()).build());
            return;
        }

        Message message = new Message();
        message.setEvent("requestTokens");
        message.setService("token_service");
        message.setPayload(token);
        message.setCallback(new Callback("customer_service", "requestTokensResponse"));
        message.setRequestId(responseHandler.saveRestResponseObject(response));
        this.sendMessage(message);

    }


    // @TODO: Missing in UML
    // @Status: implemented
    public void requestReport(ReportRequestDTO reportRequestDTO, AsyncResponse response) {

        Message message = new Message();
        message.setEvent("getCustomerReport");
        message.setService("reporting_service");
        message.setPayload(reportRequestDTO);
        message.setCallback(new Callback("customer_service", "receiveReport"));
        message.setRequestId(responseHandler.saveRestResponseObject(response));
        UUID requestId = responseHandler.saveRestResponseObject(response);
        message.setRequestId(requestId);
        this.sendMessage(message);

    }

    public void receiveReport(Message message, JsonObject payload){
        AsyncResponse response = this.responseHandler.getRestResponseObject(message.getRequestId());
        Report report = gson.fromJson(payload.toString(), Report.class);
        response.resume(Response.status(message.getStatus()).entity(report));
    }

    // @Status: In dispute / in partial implemented
    /*
    If not irrelevant please notify Benjamin for UML update
    public void getUnusedToken(UUID customerID, AsyncResponse response) {
        try {
            UUID tokenID = customerService.getUnusedToken(customerID);
            response.resume(Response.status(200).entity(gson.toJson(tokenID)));
        } catch (CustomerException ce) {
            response.resume(Response.status(400).entity(ce.getMessage()));
        }
    }
    */

    /*
        Handle REST Async call responses
     */

    // @TODO: Missing in UML
    // @Status: Implemented
    public void requestRefundResponse(Message message, JsonObject payload){
        AsyncResponse response = this.responseHandler.getRestResponseObject(message.getRequestId());
        response.resume(Response.status(message.getStatus()).entity(message.getStatusMessage()));
    }

    // @TODO: Missing in UML
    // @Status: Implemented
    public void requestTokensResponse(Message message, JsonObject payload){
        AsyncResponse response = responseHandler.getRestResponseObject(message.getRequestId());

        UUID customerId = UUID.fromString(payload.get("customerID").toString().replace("\"", ""));
        List<String> tokenIdsInString = gson.fromJson(payload.get("tokenIDs").toString(), List.class);
        List<UUID> tokenIds = new ArrayList<>();

        tokenIdsInString.forEach((tokenId) -> {
            tokenIds.add(UUID.fromString(tokenId));
        });

        try {
            customerService.addTokens(customerId, tokenIds);
            response.resume(Response.status(message.getStatus()).entity(payload.get("tokenIDs")).build());
        } catch (Exception e){
            response.resume(Response.status(500).build());
            e.printStackTrace();
        }
    }




}
