package dto;

import models.Payload;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReceiveTokensDTO extends Payload {

    private List<UUID> tokens;


    public List<UUID> getTokens() {
        return tokens;
    }

    public void setTokens(List<UUID> tokens) {
        this.tokens = tokens;
    }


}
