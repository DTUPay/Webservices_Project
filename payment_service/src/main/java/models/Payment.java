package models;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Payment extends Payload{
    private final UUID paymentID;
    private final UUID merchantID;
    private UUID tokenID;
    private UUID refundTokenID;
    private UUID customerID;
    private String customerAccountID;
    private String merchantAccountID;
    private final Date date;
    private final float amount;
    private PaymentStatus paymentStatus;

    /**
     * Create pending Payment
     * @param merchantID 
     * @param amount 
     */
    public Payment(UUID merchantID, float amount) {
        this.merchantID = merchantID;
        this.amount = amount;
        this.paymentStatus = PaymentStatus.PENDING;
        this.date = Calendar.getInstance().getTime();
        this.paymentID = UUID.randomUUID();
    }

    public UUID getPaymentID() {
        return paymentID;
    }

    public UUID getMerchantID() {
        return merchantID;
    }

    public UUID getTokenID() {
        return tokenID;
    }

    public void setTokenID(UUID tokenID) {
        this.tokenID = tokenID;
    }

    public java.util.Date getDate() {
        return date;
    }

    public float getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return paymentStatus;
    }

    public void setStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public UUID getCustomerID() {
        return customerID;
    }

    public void setCustomerID(UUID uuid) {
        this.customerID = uuid;
    }

    public String getCustomerAccountID() {
        return customerAccountID;
    }

    public void setCustomerAccountID(String customerAccountID) {
        this.customerAccountID = customerAccountID;
    }

    public String getMerchantAccountID() {
        return merchantAccountID;
    }

    public void setMerchantAccountID(String merchantAccountID) {
        this.merchantAccountID = merchantAccountID;
    }

    public UUID getRefundTokenID() {
        return refundTokenID;
    }

    public void setRefundTokenID(UUID refundTokenID) {
        this.refundTokenID = refundTokenID;
    }
}
