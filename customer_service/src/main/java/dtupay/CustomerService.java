/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */

package dtupay;

import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CustomerService {
    RabbitMqTest rabbitMqTest;
    HashMap<UUID, AsyncResponse> pendingRequests = new HashMap<>();

    public CustomerService(){
        try {
            this.rabbitMqTest = new RabbitMqTest("customer_service", this);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public UUID addPendingRequest(AsyncResponse asyncResponse){
        UUID uuid = UUID.randomUUID();
        pendingRequests.put(uuid, asyncResponse);
        return uuid;
    }

    public void processMessage(JsonObject jsonObject){
        String action = jsonObject.get("hello").toString().replaceAll("\"", "");
        String uuidString = jsonObject.get("uuid").toString().replaceAll("\"", "").replaceAll("\\\\", "");
        System.out.println("UUID: " + uuidString);
        System.out.println("UUID length: " + uuidString.length());
        UUID uuid = UUID.fromString(uuidString);
        System.out.println("Customer_service received message: " + action);

        switch (action){
            case "hello":
                System.out.println("Yesss it worked!!");
                System.out.println("Pending requests: " + pendingRequests.size());
                pendingRequests.get(uuid).resume(Response.status(202).build());
                pendingRequests.remove(uuid);
                break;
        }
    }
}
