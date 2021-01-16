package dto;

import models.Payload;

import java.util.UUID;

public class MerchantIDDTO extends Payload {
    private UUID merchantID;

    public UUID getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(UUID merchantID) {
        this.merchantID = merchantID;
    }
}
