package dtupay;

import javax.ws.rs.container.AsyncResponse;
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

    public boolean containsRestResponseObject(UUID requestId){
        return this.pendingRequests.containsKey(requestId);
    }

    public AsyncResponse getRestResponseObject(UUID requestId){
        return this.pendingRequests.get(requestId);
    }

    public void removeRestResponseObject(UUID requestId){
        pendingRequests.remove(requestId);
    }

    public UUID saveRestResponseObject(AsyncResponse response){
        UUID uuid = UUID.randomUUID();
        pendingRequests.put(uuid, response);
        return uuid;
    }

    public Integer getSizeOfPendingRequests() {
        return this.pendingRequests.size();
    }
}
