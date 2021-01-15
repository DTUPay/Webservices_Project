package dtupay;

import models.Message;

import javax.ws.rs.container.AsyncResponse;
import java.util.HashMap;
import java.util.UUID;

public class RestResponseHandler {
    public HashMap<UUID, AsyncResponse> pendingRequests = new HashMap<>();

    public RestResponseHandler(){

    }

    boolean containsRestResponseObject(UUID requestId){
        return this.pendingRequests.containsKey(requestId);
    }

    AsyncResponse getRestResponseObject(UUID requestId){
        return this.pendingRequests.get(requestId);
    }

    void removeRestResponseObject(UUID requestId){
        System.out.println("Removing key: " + requestId);
        pendingRequests.remove(requestId);
    }

    void saveRestResponseObject(UUID requestId, AsyncResponse response){
        pendingRequests.put(requestId, response);
    }

    public Integer getSizeOfPendingRequests() {
        return this.pendingRequests.size();
    }
}
