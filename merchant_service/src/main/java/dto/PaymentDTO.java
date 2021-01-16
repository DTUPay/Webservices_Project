package dto;

import models.Payload;

import java.util.UUID;

public class PaymentDTO extends Payload {
    private UUID merchantID;
    private float amount;
    private UUID tokenID;

    public UUID getTokenID() {
        return tokenID;
    }

    public UUID getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(UUID merchantID) {
        this.merchantID = merchantID;
    }

    public void setTokenID(UUID tokenID) {
        this.tokenID = tokenID;
    }


    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
