package servants;

import org.jboss.resteasy.tracing.providers.jsonb.JSONBJsonFormatRESTEasyTracingInfo;
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
    private String id;
    private List<UUID> customerTokens;

    public CustomerServant(String id) {
        this.id = id;
        this.customerTokens = new ArrayList<>();
    }

    public void acceptPayment(String paymentID, String tokenID) throws Exception {
        RestCommunicator communicator = new RestCommunicator(Service.CUSTOMER.port);
        String path = Service.CUSTOMER.port + "/merchant";
        JsonObject payment = Json.createObjectBuilder()
                .add("paymentID", paymentID)
                .add("tokenID", tokenID).build();
        Object responseEntity = communicator.post(payment,path);
    }

    public void requestTokens(Integer requestedTokens) throws Exception {
        if(requestedTokens == 0)
            return;
        RestCommunicator communicator = new RestCommunicator(Service.CUSTOMER.port);
        String path = Service.CUSTOMER.port + "/tokens";
            Object responseEntity = communicator.post(requestedTokens,path);
            if(verifyList(responseEntity, UUID.class))
                addTokens((List<?>)responseEntity);
            else throw new Exception("The returned entity type did not match List<String>!");

    }

    public void requestRefund(String paymentID) throws Exception {
        RestCommunicator communicator = new RestCommunicator(Service.CUSTOMER.port);
        String path = Service.CUSTOMER.port + "merchant";
        boolean success = communicator.put(paymentID, path);
    }

    private void addTokens(List list) {
        for(Object object : list) {
            this.customerTokens.add((UUID) object);
        }

    }

    private <T> boolean verifyList(Object responseEntity, Class<T> type) {
        if(responseEntity instanceof List<?>) {
            List<?> list = (List<?>) responseEntity;
            for (Object obj : list) {
                if(!type.isInstance(obj))
                    return false;
            }
            return true;
        }
        return false;
    }

    public List<UUID> getCustomerTokens() {
        return this.customerTokens;
    }
}
