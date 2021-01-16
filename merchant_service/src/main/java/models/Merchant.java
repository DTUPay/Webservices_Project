package models;

import java.util.UUID;

public class Merchant extends Payload{
    private String name;
    private UUID merchantID;

    public Merchant(String name, UUID merchantID){
        this.merchantID = merchantID;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public UUID getMerchantID() {
        return merchantID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMerchantID(UUID merchantID) {
        this.merchantID = merchantID;
    }

}
