package dto;

import models.Payload;
import models.Token;

import java.util.UUID;

public class TokenDTO extends Payload {
    private UUID customerID;
    private UUID tokenID;

    public TokenDTO(Token token){
        this.customerID = token.getCustomerID();
        this.tokenID = token.getTokenID();
    }

    public UUID getCustomerID() {
        return customerID;
    }

    public void setCustomerID(UUID customerID) {
        this.customerID = customerID;
    }

    public UUID getTokenID() {
        return tokenID;
    }

    public void setTokenID(UUID tokenID) {
        this.tokenID = tokenID;
    }
}
