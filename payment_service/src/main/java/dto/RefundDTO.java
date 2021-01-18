package dto;

import java.util.UUID;

public class RefundDTO {
    private UUID tokenID;
    private UUID paymentID;

    public UUID getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(UUID paymentID) {
        this.paymentID = paymentID;
    }

    public UUID getTokenID() {
        return tokenID;
    }

    public void setTokenID(UUID tokenID) {
        this.tokenID = tokenID;
    }
}
