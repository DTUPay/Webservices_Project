package dtupay;

import models.Message;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.UUID;

public class RestResponseHandler {
    public HashMap<UUID, AsyncResponse> pendingRequests = new HashMap<>();
    private static RestResponseHandler instance = new RestResponseHandler();


    private RestResponseHandler(){}

    //Get the only object available
    public static RestResponseHandler getInstance(){
        return instance;
    }

    boolean containsRestResponseObject(UUID requestId){
        return this.pendingRequests.containsKey(requestId);
    }

    AsyncResponse getRestResponseObject(UUID requestId){
        return this.pendingRequests.get(requestId);
    }

    void removeRestResponseObject(UUID requestId){
        pendingRequests.remove(requestId);
    }

    void saveRestResponseObject(UUID requestId, AsyncResponse response){
        pendingRequests.put(requestId, response);
    }

    public Integer getSizeOfPendingRequests() {
        return this.pendingRequests.size();
    }
}
