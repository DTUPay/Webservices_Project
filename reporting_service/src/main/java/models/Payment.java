package models;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @author Mikkel Rosenfeldt Anderson & Laura
 */
public class Payment extends Payload{
    private final UUID paymentID;
    private final UUID merchantID;
    private UUID tokenID;
    private UUID refundTokenID;
    private UUID customerID;
    private String customerAccountID;
    private String merchantAccountID;
    protected Date date;
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
        this.paymentStatus = PaymentStatus.COMPLETED;
        this.date = Calendar.getInstance().getTime();
        this.paymentID = UUID.randomUUID();
    }

    public Payment(Payment payment) {
        this.paymentID = payment.getPaymentID();
        this.merchantID = payment.getMerchantID();
        this.tokenID = payment.getTokenID();
        this.refundTokenID = payment.getRefundTokenID();
        this.customerID = payment.getCustomerID();
        this.customerAccountID = payment.getCustomerAccountID();
        this.merchantAccountID = payment.getMerchantAccountID();
        this.date = payment.getDate();
        this.amount = payment.getAmount();
        this.paymentStatus = payment.getStatus();
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

    public Date getDate() {
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
