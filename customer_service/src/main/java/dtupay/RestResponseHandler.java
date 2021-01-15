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

    UUID saveRestResponseObject(AsyncResponse response){
        UUID uuid = UUID.randomUUID();
        pendingRequests.put(uuid, response);
        return uuid;
    }

    public Integer getSizeOfPendingRequests() {
        return this.pendingRequests.size();
    }
}
