package models;

import java.util.UUID;

public class Token {
    private final UUID tokenID;
    private UUID customerID;
    private boolean isUsed;

    public Token(UUID customerID) {
        this.tokenID = UUID.randomUUID();
        this.customerID = customerID;
        this.isUsed = false;
    }

    public UUID getTokenID() {
        return this.tokenID;
    }

    public UUID getCustomerID() {
        return this.customerID;
    }

    public boolean isUsed() {
        return this.isUsed;
    }

    public void setUsed(boolean used) {
        this.isUsed = used;
    }
}
