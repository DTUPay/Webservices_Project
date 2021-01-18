package dto;

import models.Payload;

import java.util.UUID;

public class PaymentDTO extends Payload {
    private UUID merchantID;
    private float amount;
    private UUID tokenID;
    private String merchantAccountID;

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

    public String getMerchantAccountID() {
        return merchantAccountID;
    }

    public void setMerchantAccountID(String merchantAccountID) {
        this.merchantAccountID = merchantAccountID;
    }
}
