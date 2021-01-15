package dtupay;

import exceptions.MerchantException;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import models.Merchant;

import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.UUID;

@QuarkusMain
public class MerchantService {
    RabbitMq rabbitMq;
    public HashMap<UUID, AsyncResponse> pendingRequests = new HashMap<>();
    IMerchantRepository merchantRepository = new MerchantRepository();

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

    public void registerMerchant(Merchant merchant) throws MerchantException {
        if (!merchantRepository.hasMerchant(merchant.getCVR())) {
            merchantRepository.addMerchant(merchant);
        }
        else throw new MerchantException("Merchant with CVR: " + merchant.getCVR() + " already exists");
    }

    public void removeMerchant(String cvr) throws MerchantException {

        if (merchantRepository.hasMerchant(cvr)) {
            merchantRepository.removeMerchant(cvr);
        }
        else throw new MerchantException("Merchant with CVR: " + cvr + " doesn't exist");
    }

    public Merchant getMerchant(String cvr) throws MerchantException {
        if (merchantRepository.hasMerchant(cvr)) {
            return merchantRepository.getMerchant(cvr);
        }
        else throw new MerchantException("Merchant with CVR: " + cvr + " doesn't exist");
    }
}
