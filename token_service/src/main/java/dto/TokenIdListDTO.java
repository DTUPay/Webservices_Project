package dto;

import models.Payload;

import java.util.List;
import java.util.UUID;

public class TokenIdListDTO extends Payload {
    List<UUID> tokenIds;

    public List<UUID> getTokenIds() {
        return tokenIds;
    }

    public void setTokenIds(List<UUID> tokenIds) {
        this.tokenIds = tokenIds;
    }
}
