package dto;

import models.Payload;

import java.util.List;
import java.util.UUID;

public class TokenIdListDTO extends Payload {
    List<UUID> tokenIDs;
    UUID customerID;

    public List<UUID> getTokenIDs() {
        return tokenIDs;
    }

    public void setTokenIDs(List<UUID> tokenIDs) {
        this.tokenIDs = tokenIDs;
    }

    public UUID getCustomerID() {
        return customerID;
    }

    public void setCustomerID(UUID customerID) {
        this.customerID = customerID;
    }
}
