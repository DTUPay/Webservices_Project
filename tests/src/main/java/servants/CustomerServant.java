package servants;

import servants.RestCommunicator.Service;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Björn Wilting s184214
 */
public class CustomerServant {
    private Customer customer;
    private UUID id;
    private List<UUID> customerTokens;

    public CustomerServant(UUID id) {
        this.id = id;
        this.customerTokens = new ArrayList<>();
    }

    public UUID getID() {
        return this.id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void requestTokens(UUID customerID, Integer requestedTokens) throws Exception {
        if(requestedTokens == 0) return;
        RestCommunicator communicator = new RestCommunicator(Service.CUSTOMER);
        String path = "/tokens";
        JsonObject tokenRequest = Json.createObjectBuilder()
                .add("customerID", customerID.toString())
                .add("amount", requestedTokens)
                .build();
        Object responseEntity = communicator.post(tokenRequest,path, 200);
        addTokens(convertListToUUID(responseEntity));
    }

    //TODO: Change message signature to have customerID, merchantID and paymentID
    public boolean requestRefund(UUID customerID, UUID merchantID, UUID paymentID) {
        RestCommunicator communicator = new RestCommunicator(Service.CUSTOMER);
        String path = "/refund";
        JsonObject refundDto = Json.createObjectBuilder()
                .add("customerID", customerID.toString())
                .add("merchantID", merchantID.toString())
                .add("paymentID", paymentID.toString())
                .build();
        try {
            return communicator.put(refundDto, path);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<?> requestReport() throws Exception {
        throw new Exception("Not implemented!");
    }


    private void addTokens(List list) {
        for(Object object : list) {
            this.customerTokens.add((UUID) object);
        }

    }

    private <T> List<UUID> convertListToUUID(Object responseEntity) throws Exception {
        if (responseEntity instanceof List<?>) {
            List<?> arrayList = (List<?>) responseEntity;
            ArrayList<UUID> uuids = new ArrayList<>();
            for (Object obj : arrayList) {
                if (!String.class.isInstance(obj))
                    throw new Exception("The list data did not match type " + String.class.toString());
                uuids.add(UUID.fromString((String) obj));
            }
            return uuids;
        }
        throw new Exception("Input object is not an instance of List<?>!");
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
