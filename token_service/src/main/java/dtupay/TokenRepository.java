package dtupay;

import exceptions.TokenException;
import models.Token;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class TokenRepository implements ITokenRepository {

    HashMap<UUID, Token> tokens = new HashMap<>();

    @Override
    public ArrayList<UUID> addTokens(int customerID, int amount) {
        //TODO: Verify amount of tokens!
        ArrayList<UUID> tokenIDs = new ArrayList<>();
        for (int i = 0; i< amount; i++) {
            Token token = new Token(customerID);
            tokens.put(token.getTokenID(),token);
            tokenIDs.add(token.getTokenID());
        }


        return tokenIDs;
    }

    @Override
    public void useToken(UUID tokenID) throws TokenException {
        if (tokens.containsKey(tokenID)) {
            if (!tokens.get(tokenID).isUsed()){
                tokens.get(tokenID).setUsed(true);
            } else {
                throw new TokenException("Token has already been used");
            }
        }
        throw new TokenException("Token doesn't exist");
    }

    @Override
    public boolean isTokenValid(UUID tokenID, int customerID) throws TokenException {
        if (this.tokens.containsKey(tokenID)) {
            return this.tokens.get(tokenID).getCustomerID() == customerID && !this.tokens.get(tokenID).isUsed();
        }
        throw new TokenException("Token doesn't exist");
    }

    @Override
    public void processMessage(JsonObject jsonObject) {

    }

}
