package models;

import java.util.UUID;

public class Token {
    private final UUID tokenID;
    private String customerID;
    private boolean isUsed;

    public Token(String customerID) {
        this.tokenID = UUID.randomUUID();
        this.customerID = customerID;
        this.isUsed = false;
    }

    public UUID getTokenID() {
        return this.tokenID;
    }

    public String getCustomerID() {
        return this.customerID;
    }

    public boolean isUsed() {
        return this.isUsed;
    }

    public void setUsed(boolean used) {
        this.isUsed = used;
    }
}
