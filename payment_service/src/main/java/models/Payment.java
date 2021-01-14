package models;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Payment {
    private UUID paymentID;
    private int merchantID;
    private int tokenID;
    private Date date;
    private int amount;
    private PaymentStatus paymentStatus;

    /**
     * Create pending Payment
     * @param merchantID 
     * @param amount 
     */
    public Payment(int merchantID, int amount) {
        this.merchantID = merchantID;
        this.amount = amount;
        this.paymentStatus = PaymentStatus.PENDING;
        this.date = Calendar.getInstance().getTime();
        this.paymentID = UUID.randomUUID();

    }

    public UUID getPaymentID() {
        return paymentID;
    }

    public int getMerchantID() {
        return merchantID;
    }

    public int getTokenID() {
        return tokenID;
    }

    public void setTokenID(int tokenID) {
        tokenID = tokenID;
    }

    public java.util.Date getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return paymentStatus;
    }

    public void setStatus(PaymentStatus paymentStatus) {
        paymentStatus = paymentStatus;
    }
}
