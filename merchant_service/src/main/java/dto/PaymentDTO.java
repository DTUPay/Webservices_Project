package dto;

import models.Payload;

import java.util.UUID;

public class PaymentDTO extends Payload {
    private String merchantID;
    private UUID paymentID;
    private UUID tokenID;

    public UUID getTokenID() {
        return tokenID;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public void setTokenID(UUID tokenID) {
        this.tokenID = tokenID;
    }


    public UUID getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(UUID paymentID) {
        this.paymentID = paymentID;
    }
}
