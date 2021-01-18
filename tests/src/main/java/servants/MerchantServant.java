package servants;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.UUID;

/**
 * @author Laura Hansen s184234
 */
public class MerchantServant {
    private Merchant merchant;
    private UUID id;


    public MerchantServant(UUID id) {
        this.id = id;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean requestPayment(int amount, UUID merchantID, UUID tokenID) throws Exception {
        RestCommunicator communicator = new RestCommunicator(RestCommunicator.Service.MERCHANT);
        JsonObject payment = Json.createObjectBuilder()
                .add("amount", amount)
                .add("merchantID", merchantID.toString())
                .add("tokenID", tokenID.toString())
                .add("merchantAccountID", "Empty...")
                .build();
        try {
            communicator.post(payment,"/payment", 200);
        } catch (Exception e){
            return false;
        }

        return true;
    }
    
    

    public void generateReport() {

    }
}
