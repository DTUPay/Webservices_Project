package dto;

import models.Payload;

import java.util.UUID;

public class MerchantDTO extends Payload {
    private String name;
    private UUID merchantID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(UUID merchantID) {
        this.merchantID = merchantID;
    }
}
