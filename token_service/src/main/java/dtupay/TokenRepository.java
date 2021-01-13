package dtupay;

import models.Token;

import java.util.HashMap;
import java.util.UUID;

public class TokenRepository implements ITokenRepository {

    HashMap<UUID, Token> tokens = new HashMap<>();

    @Override
    public void addTokens(String customerID, int amount) throws Exception {
        //TODO: Verify amount of tokens!
        Token token = new Token();
        tokens.put(token.getTokenID(),token);
    }

    @Override
    public void useToken(UUID tokenID) throws Exception {

    }
}
