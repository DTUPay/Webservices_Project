package servants;

import servants.RestCommunicator.Service;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Björn Wilting s184214
 */
public class CustomerServant {
    private Customer customer;
    private String id;
    private List<UUID> customerTokens;

    public CustomerServant(String id) {
        this.id = id;
        this.customerTokens = new ArrayList<>();
    }

    public String getID() {
        return this.id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void acceptPayment(int amount, String merchantID, UUID tokenID) throws Exception {
        RestCommunicator communicator = new RestCommunicator(Service.CUSTOMER.port);
        JsonObject payment = Json.createObjectBuilder()
                .add("amount", amount)
                .add("merchantID", merchantID)
                .add("uuid", tokenID.toString())
                .build();
        communicator.post(payment,Service.CUSTOMER.port + "/" + Service.CUSTOMER.location + "`/merchant");

    }

    public void requestTokens(String customerID, Integer requestedTokens) throws Exception {
        if(requestedTokens == 0) return;
        RestCommunicator communicator = new RestCommunicator(Service.CUSTOMER.port);
        String path = Service.CUSTOMER.port + "/" + Service.CUSTOMER.location + "/tokens";
        JsonObject tokenRequest = Json.createObjectBuilder()
                .add("customerID", customerID)
                .add("amount", requestedTokens)
                .build();
            Object responseEntity = communicator.post(tokenRequest,path);
            if(verifyList(responseEntity, UUID.class))
                addTokens((List<?>)responseEntity);
            else throw new Exception("The returned entity type did not match List<String>!");

    }

    //TODO: Change message signature to have customerID, merchantID and paymentID
    public void requestRefund(String paymentID) throws Exception {
        RestCommunicator communicator = new RestCommunicator(Service.CUSTOMER.port);
        String path = Service.CUSTOMER.port + "/" + Service.CUSTOMER.location + "refund";
        boolean success = communicator.put(paymentID, path);
        if(success)
            return;
        throw new Exception("Refunding the payment: " + paymentID + " failed!");
    }

    private void addTokens(List list) {
        for(Object object : list) {
            this.customerTokens.add((UUID) object);
        }

    }

    private <T> boolean verifyList(Object responseEntity, Class<T> type) {
        if(responseEntity instanceof List<?>) {
            List<?> list = (List<?>) responseEntity;
            for (Object obj : list)
                if(!type.isInstance(obj)) return false;
            return true;
        }
        return false;
    }

    public List<UUID> getCustomerTokens() {
        return this.customerTokens;
    }

    public UUID selectToken() throws Exception {
        if(customerTokens.size() == 0)
            throw new Exception("No token availiable!");
        UUID token = this.customerTokens.get(customerTokens.size()-1);
        customerTokens.remove(customerTokens.size()-1);
        return token;
    }
}
