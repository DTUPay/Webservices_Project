package dtupay;

import javax.ws.rs.container.AsyncResponse;
import java.util.HashMap;
import java.util.UUID;

public class RestResponseHandler {
    public HashMap<UUID, AsyncResponse> pendingRequests = new HashMap<>();

    public RestResponseHandler(){

    }

    boolean containsRestResponseObject(UUID messageId){
        return this.pendingRequests.containsKey(messageId);
    }

    AsyncResponse getRestResponseObject(UUID messageId){
        return this.pendingRequests.get(messageId);
    }

    void removeRestResponseObject(UUID messageId){
        System.out.println("Removing key: " + messageId);
        pendingRequests.remove(messageId);
    }

    void saveRestResponseObject(UUID messageId, AsyncResponse response){
        pendingRequests.put(messageId, response);
    }

    public Integer getSizeOfPendingRequests() {
        return this.pendingRequests.size();
    }
}
