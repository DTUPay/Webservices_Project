package dtupay;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.UUID;

@QuarkusMain
public class MerchantService {
    RabbitMq rabbitMq;
    public HashMap<UUID, AsyncResponse> pendingRequests = new HashMap<>();

    public MerchantService() {
        try {
            String serviceName = System.getenv("SERVICE_NAME"); //merchant_service
            System.out.println(serviceName + " started");
            this.rabbitMq = new RabbitMq(serviceName, this);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void main(String[] args) {
        MerchantService service = new MerchantService();
        Quarkus.run();
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
