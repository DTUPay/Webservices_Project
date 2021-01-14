package dtupay;

import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.UUID;

public class MerchantService {
    RabbitMq rabbitMq;
    public HashMap<UUID, AsyncResponse> pendingRequests = new HashMap<>();

    public MerchantService() {
        try {
            this.rabbitMq = new RabbitMq("customer_service", this);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public UUID addPendingRequest(AsyncResponse asyncResponse){
        UUID uuid = UUID.randomUUID();
        pendingRequests.put(uuid, asyncResponse);
        return uuid;
    }

    public void respondPendingRequest(UUID uuid){
        pendingRequests.get(uuid).resume(Response.status(202).build());
        pendingRequests.remove(uuid);
    }

    public void demo(JsonObject jsonObject){
        // UUID needs to be trimmed after convertion from JSON
        String uuidString = jsonObject.get("uuid").toString().replaceAll("\"", "").replaceAll("\\\\", "");
        UUID uuid = UUID.fromString(uuidString);
        respondPendingRequest(uuid);
    }
}
