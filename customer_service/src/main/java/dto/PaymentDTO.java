package dto;

import models.Payload;

import java.util.UUID;

public class PaymentDTO extends Payload {
    private String customerID;
    private String merchantID;
    private UUID paymentID;

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getMechantID() {
        return merchantID;
    }

    public void setMechantID(String mechantID) {
        this.merchantID = mechantID;
    }

    public UUID getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(UUID paymentID) {
        this.paymentID = paymentID;
    }
}
