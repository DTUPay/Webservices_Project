package dto;

import models.Payload;

import java.util.UUID;

public class TokenIDDTO extends Payload {
    public UUID tokenID;

    public UUID getTokenID() {
        return tokenID;
    }

    public void setTokenID(UUID tokenID) {
        this.tokenID = tokenID;
    }
}
