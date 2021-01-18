package dtupay;
// @author: Rubatharisan Thirumathyam

import brokers.ManagementBroker;
import com.google.gson.Gson;
import dto.CustomerDTO;
import dto.MerchantDTO;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import models.Callback;
import models.Message;

import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.util.UUID;

@QuarkusMain
public class ManagementService {
    ManagementBroker broker;
    private static ManagementService instance = new ManagementService();
    RestResponseHandler RestfulHandler = RestResponseHandler.getInstance();
    Gson gson = new Gson();

    public ManagementService() {
        broker = new ManagementBroker(this);
    }

    public static ManagementService getInstance(){
        return instance;
    }

    public static void main(String[] args) {
        ManagementService service = ManagementService.getInstance();
        Quarkus.run();
    }

    // @TODO: Missing in UML
    // @Status: Implemented
    public void registerCustomer(CustomerDTO customer, AsyncResponse response){
        Message message = new Message();

        message.setService("customer_service");
        message.setEvent("registerCustomer");
        message.setPayload(customer);
        message.setCallback(new Callback("management_service", "registerCustomerResponse"));

        UUID requestId = RestfulHandler.saveRestResponseObject(response);
        message.setRequestId(requestId);

        this.broker.sendMessage(message);
    }

    // @TODO: Missing in UML
    // @Status: Implemented
    public void registerCustomerResponse(Message message, JsonObject payload){
        AsyncResponse response = RestfulHandler.getRestResponseObject(message.getRequestId());
        if(message.getStatus() == 201){
            response.resume(Response.status(message.getStatus()).entity(payload).build());
        } else {
            response.resume(Response.status(message.getStatus()).entity(message.getStatusMessage()).build());
        }
    }

    // @TODO: Missing in UML
    // @Status: Implemented
    public void removeCustomer(String customerID, AsyncResponse response){
        Message message = new Message();

        message.setService("customer_service");
        message.setEvent("removeCustomer");

        CustomerDTO customer = new CustomerDTO();
        customer.setCustomerID(customerID);

        message.setPayload(customer);
        message.setCallback(new Callback("management_service", "removeCustomerResponse"));

        UUID requestId = RestfulHandler.saveRestResponseObject(response);
        message.setRequestId(requestId);

        this.broker.sendMessage(message);
    }

    // @TODO: Missing in UML
    // @Status: Implemented
    public void removeCustomerResponse(Message message){
        AsyncResponse response = RestfulHandler.getRestResponseObject(message.getRequestId());
        response.resume(Response.status(message.getStatus()).entity(message.getStatusMessage()).build());
    }


    // @TODO: Missing in UML
    // @Status: Implemented
    public void registerMerchant(MerchantDTO merchant, AsyncResponse response) {
        Message message = new Message();

        message.setService("merchant_service");
        message.setEvent("registerMerchant");
        message.setPayload(merchant);
        message.setCallback(new Callback("management_service", "registerMerchantResponse"));

        UUID requestId = RestfulHandler.saveRestResponseObject(response);
        message.setRequestId(requestId);

        this.broker.sendMessage(message);
    }

    // @TODO: Missing in UML
    // @Status: Implemented
    public void registerMerchantResponse(Message message, JsonObject payload){
        AsyncResponse response = RestfulHandler.getRestResponseObject(message.getRequestId());
        if(message.getStatus() == 201){
            response.resume(Response.status(message.getStatus()).entity(payload).build());
        } else {
            response.resume(Response.status(message.getStatus()).entity(message.getStatusMessage()).build());
        }
    }

    // @TODO: Missing in UML
    // @Status: Implemented
    public void removeMerchant(String merchantID, AsyncResponse response){
        Message message = new Message();

        message.setService("merchant_service");
        message.setEvent("removeMerchant");

        MerchantDTO customer = new MerchantDTO();
        customer.setMerchantID(merchantID);

        message.setPayload(customer);
        message.setCallback(new Callback("management_service", "removeMerchantResponse"));

        UUID requestId = RestfulHandler.saveRestResponseObject(response);
        message.setRequestId(requestId);

        this.broker.sendMessage(message);
    }

    // @TODO: Missing in UML
    // @Status: Implemented
    public void removeMerchantResponse(Message message){
        AsyncResponse response = RestfulHandler.getRestResponseObject(message.getRequestId());
        response.resume(Response.status(message.getStatus()).entity(message.getStatusMessage()).build());
    }
}
