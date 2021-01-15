/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen
 */

package dtupay;


import brokers.CustomerBroker;
import dto.PaymentDTO;
import dto.TokensDTO;
import exceptions.CustomerException;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import models.Callback;
import models.Customer;
import models.Message;

import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.util.UUID;

@QuarkusMain
public class CustomerService {
    CustomerBroker broker;
    ICustomerRepository customerRepository = new CustomerRepository();
    private static CustomerService instance = new CustomerService();
    RestResponseHandler RestfulHandler = RestResponseHandler.getInstance();

    public CustomerService() {
        broker = new CustomerBroker(this);
    }

    public static CustomerService getInstance(){
        return instance;
    }

    public static void main(String[] args) {
        CustomerService service = CustomerService.getInstance();
        Quarkus.run();
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

    // @TODO: Missing in UML
    // @Status: Implemented
    public void requestRefund(PaymentDTO payment, AsyncResponse response){
        UUID requestId = UUID.randomUUID();

        Message message = new Message();
        message.setEvent("requestRefund");
        message.setService("payment_service");
        message.setRequestId(requestId);
        message.setPayload(payment);
        message.setCallback(new Callback("customer_service", "requestRefundResponse"));

        this.broker.sendMessage(message);
        RestfulHandler.saveRestResponseObject(response);
    }

    // @TODO: Missing in UML
    // @Status: Implemented
    public void requestRefundResponse(Message message, JsonObject payload){
        AsyncResponse response = RestfulHandler.getRestResponseObject(message.getRequestId());
        response.resume(Response.status(message.getStatus()).entity(message.getStatusMessage()));
    }

    // @TODO: Missing in UML
    // @Status: implemented
    public void requestTokens(TokensDTO token, AsyncResponse response) {
        UUID requestId = UUID.randomUUID();

        Message message = new Message();
        message.setEvent("requestTokens");
        message.setService("token_service");
        message.setRequestId(requestId);
        message.setPayload(token);
        message.setCallback(new Callback("customer_service", "requestTokensResponse"));

        this.broker.sendMessage(message);
        RestfulHandler.saveRestResponseObject(response);
    }

    // @TODO: Missing in UML
    // @Status: Implemented
    public void requestTokensResponse(Message message, JsonObject payload){
        AsyncResponse response = RestfulHandler.getRestResponseObject(message.getRequestId());
        response.resume(Response.status(message.getStatus()).entity(message.getStatusMessage()));
    }

}
