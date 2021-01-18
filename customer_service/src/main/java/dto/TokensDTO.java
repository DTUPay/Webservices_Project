package dto;
// @author: Rubatharisan Thirumathyam
import models.Payload;

import java.util.UUID;

public class TokensDTO extends Payload {
    private UUID customerID;
    private int amount;

    public UUID getCustomerID() {
        return customerID;
    }

    public void setCustomerID(UUID customerID) {
        this.customerID = customerID;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
