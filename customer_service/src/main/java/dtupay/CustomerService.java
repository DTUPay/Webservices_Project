/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen
 */

package dtupay;

import brokers.RabbitMQ;
import com.rabbitmq.client.DeliverCallback;
import dto.ReceiveTokensDTO;
import exceptions.CustomerException;
import io.cucumber.messages.internal.com.google.gson.Gson;
import models.Customer;
import models.Message;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.util.UUID;

public class CustomerService {
    RabbitMQ broker;
    Gson gson = new Gson();
    DeliverCallback deliverCallback;
    String queue = "customer_service";
    RestResponseHandler responseHandler;

    ICustomerRepository customerRepository = new CustomerRepository();

    public CustomerService() {
        responseHandler = RestResponseHandler.getInstance();

        try {
            this.broker = new RabbitMQ(queue);
            this.listenOnQueue(queue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testReceiveTokens(Message message, AsyncResponse response) throws Exception {
        UUID uuid = UUID.randomUUID();
        message.setRequestId(uuid);

        this.sendMessage("customer_service", message, response);
    }

    public void receiveTokens(Message message, JsonObject payload) {
        ReceiveTokensDTO dto = gson.fromJson(payload.toString(), ReceiveTokensDTO.class);

        if(responseHandler.containsRestResponseObject(message.getRequestId())){
            AsyncResponse response = responseHandler.getRestResponseObject(message.getRequestId());

            response.resume(
                    Response
                            .status(200)
                            .entity(gson.toJson(dto.getTokens()))
                            .build()
            );

            responseHandler.removeRestResponseObject(message.getRequestId());
        } else {
            System.out.println("Event has no response");
        }

    }


    public void registerCustomer(Customer customer) throws CustomerException {
        if (!customerRepository.hasCustomer(customer.getCPRNumber())) {
            customerRepository.addCustomer(customer);
        }
        else throw new CustomerException("Customer with CPR: " + customer.getCPRNumber() + " already exists");
    }

    public void removeCustomer(String cpr) throws CustomerException {
        if (customerRepository.hasCustomer(cpr)) {
            customerRepository.removeCustomer(cpr);
        }
        else throw new CustomerException("Customer with CPR: " + cpr + " doesn't exist");
    }


    public Customer getCustomer(String cpr) throws CustomerException {
        if (customerRepository.hasCustomer(cpr)) {
            return customerRepository.getCustomer(cpr);
        }
        else throw new CustomerException("Customer with CPR: " + cpr + " doesn't exist");
    }

    public boolean hasCustomer(String cprNumber) {
        return customerRepository.hasCustomer(cprNumber);
    }


    private void processMessage(Message message, JsonObject payload){

        switch(message.getEvent()) {
            case "receiveTokens":
                this.receiveTokens(message, payload);
                break;
            default:
                System.out.println("Event not handled: " + message.getEvent());
        }

    }

    private void sendMessage(String queue, Message message) throws Exception {
        try{
            broker.sendMessage(queue, gson.toJson(message));
        } catch(Exception e){
            throw new Exception(e);
        }
    }


    private void sendMessage(String queue, Message message, AsyncResponse response) throws Exception {
        responseHandler.saveRestResponseObject(message.getRequestId(), response);

        try{
            broker.sendMessage(queue, gson.toJson(message));
        } catch(Exception e){
            throw new Exception(e);
        }
    }

    private void listenOnQueue(String queue){

        deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject(); // @TODO: Validate Message, if it is JSON object

            this.processMessage(gson.fromJson(jsonObject.toString(), Message.class), jsonObject.getJsonObject("payload"));
        };

        this.broker.onQueue(queue, deliverCallback);

    }

}
