package dtupay;

import models.Payload;

import javax.ws.rs.container.AsyncResponse;
import java.util.HashMap;
import java.util.UUID;

public class PayloadHandler {
    public HashMap<UUID, Payload> payloads = new HashMap<>();
    private static PayloadHandler instance = new PayloadHandler();

    private PayloadHandler(){}

    //Get the only object available
    public static PayloadHandler getInstance(){
        return instance;
    }

    public boolean containsPayloadObject(UUID requestId){
        return this.payloads.containsKey(requestId);
    }

    public Payload getPayloadsObject(UUID requestId){
        return this.payloads.get(requestId);
    }

    public void removePayloadsObject(UUID requestId){
        payloads.remove(requestId);
    }

    public void savePayloadsObject(UUID uuid, Payload payload){
        payloads.put(uuid, payload);
    }

    public Integer getSizeOfPendingRequests() {
        return this.payloads.size();
    }
}
