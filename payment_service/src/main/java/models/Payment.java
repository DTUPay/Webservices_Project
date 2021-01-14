package models;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Payment {
    private UUID PaymentID;
    private int MerchantID;
    private int TokenID;
    private Date Date;
    private int Amount;
    private boolean IsRefunded;
    private PaymentStatus PaymentStatus;

    /**
     * Create pending Payment
     * @param merchantID 
     * @param amount 
     */
    public Payment(int merchantID, int amount) {
        MerchantID = merchantID;
        Amount = amount;
        PaymentStatus = PaymentStatus.PENDING;
        Date = Calendar.getInstance().getTime();
    }

    public UUID getPaymentID() {
        return PaymentID;
    }

    public void setPaymentID(UUID paymentID) {
        PaymentID = paymentID;
    }

    public int getMerchantID() {
        return MerchantID;
    }

    public void setMerchantID(int merchantID) {
        MerchantID = merchantID;
    }

    public int getTokenID() {
        return TokenID;
    }

    public void setTokenID(int tokenID) {
        TokenID = tokenID;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public boolean isRefunded() {
        return IsRefunded;
    }

    public void setRefunded(boolean refunded) {
        IsRefunded = refunded;
    }

    public PaymentStatus getStatus() {
        return PaymentStatus;
    }

    public void setStatus(PaymentStatus paymentStatus) {
        PaymentStatus = paymentStatus;
    }
}
