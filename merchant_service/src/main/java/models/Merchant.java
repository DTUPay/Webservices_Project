package models;

import java.util.UUID;

public class Merchant extends Payload{
    private String name;
    private UUID merchantID;
    private String accountNumber;

    public Merchant(String name, String accountNumber, UUID merchantID){
        this.merchantID = merchantID;
        this.name = name;
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public UUID getMerchantID() {
        return merchantID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMerchantID(UUID merchantID) {
        this.merchantID = merchantID;
    }

    public void setAccountNumber(String accountNumber) {
        if(this.accountNumber != null)
            return;
        this.accountNumber = accountNumber;
    }
}
