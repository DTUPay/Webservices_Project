package dto;

import models.Payload;

import java.util.UUID;

public class PaymentIDDTO extends Payload {
    private UUID paymentID;

    public PaymentIDDTO(UUID uuid){
        setPaymentID(uuid);
    }

    public UUID getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(UUID paymentID) {
        this.paymentID = paymentID;
    }
}
