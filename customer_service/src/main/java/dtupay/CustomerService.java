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
    RestResponseHandler responseHandler = new RestResponseHandler();
    RabbitMQ broker;
    Gson gson = new Gson();
    DeliverCallback deliverCallback;

    ICustomerRepository customerRepository = new CustomerRepository();

    public CustomerService() {
        String serviceName = System.getenv("SERVICE_NAME"); //customer_service
        System.out.println(serviceName + " started");
        try {
            this.broker = new RabbitMQ(serviceName);
            this.listenOnQueue(serviceName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testReceiveTokens(Message message, AsyncResponse response) throws Exception {
        UUID uuid = responseHandler.saveRestResponseObject(response);
        message.setRequestId(uuid);

        this.sendMessage(message, response);
    }

    public void receiveTokens(Message message, JsonObject payload) {
        ReceiveTokensDTO dto = gson.fromJson(payload.toString(), ReceiveTokensDTO.class);

        System.out.println("Token ids gotten: " + dto.getTokens());
        AsyncResponse response = responseHandler.getRestResponseObject(message.getRequestId());
        response.resume(
                Response
                        .status(200)
                        .entity(gson.toJson(dto.getTokens()))
                        .build()
        );
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


    private void sendMessage(Message message) throws Exception {
        try{
            broker.sendMessage(message);
        } catch(Exception e){
            throw new Exception(e);
        }
    }

    private void sendMessage(Message message, AsyncResponse response) throws Exception {
        responseHandler.saveRestResponseObject(response);

        try{
            broker.sendMessage(message);
            System.out.println("Message sent");
        } catch(Exception e){
            throw new Exception(e);
        }
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


    private void listenOnQueue(String queue){

        deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject(); // @TODO: Validate Message, if it is JSON object
            System.out.println(jsonObject.toString());

            this.processMessage(gson.fromJson(jsonObject.toString(), Message.class), jsonObject.getJsonObject("payload"));
        };

        this.broker.onQueue(queue, deliverCallback);

    }

}
