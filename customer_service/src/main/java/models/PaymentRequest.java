/*
@author Oliver O. Nielsen & Bjørn Wilting
 */

package models;

public class PaymentRequest {
    private int merchantId;
    private int amount;

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
