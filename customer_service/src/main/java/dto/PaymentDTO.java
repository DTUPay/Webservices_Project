package dto;
// @author: Rubatharisan Thirumathyam & Oliver O. Nielsen

import models.Payload;

import java.util.UUID;

public class PaymentDTO extends Payload {
    private UUID customerID;
    private UUID merchantID;
    private UUID paymentID;

    public UUID getCustomerID() {
        return customerID;
    }

    public void setCustomerID(UUID customerID) {
        this.customerID = customerID;
    }

    public UUID getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(UUID merchantID) {
        this.merchantID = merchantID;
    }

    public UUID getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(UUID paymentID) {
        this.paymentID = paymentID;
    }
}
