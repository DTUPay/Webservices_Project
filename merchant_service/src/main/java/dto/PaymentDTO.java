package dto;

import models.Payload;

import java.util.UUID;

public class PaymentDTO extends Payload {
    private String mechantID;
    private UUID paymentID;
    private UUID tokenID;

    public UUID getTokenID() {
        return tokenID;
    }

    public void setTokenID(UUID tokenID) {
        this.tokenID = tokenID;
    }

    public String getMechantID() {
        return mechantID;
    }

    public void setMechantID(String mechantID) {
        this.mechantID = mechantID;
    }

    public UUID getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(UUID paymentID) {
        this.paymentID = paymentID;
    }
}
