/*
@author Benjamin Eriksen & Rubatharisan Thirumathyam
 */

package dtupay;

import models.Token;

import java.util.HashMap;
import java.util.UUID;

public class TokenRepository implements ITokenRepository {

    HashMap<UUID, Token> tokens = new HashMap<>();
    private static TokenRepository instance = new TokenRepository();
    public static TokenRepository getInstance(){
        return instance;
    }

    @Override
    public void addToken(Token token) {
        tokens.put(token.getTokenID(),token);
    }

    @Override
    public Token getToken(UUID tokenID) {
        return tokens.get(tokenID);
    }

    @Override
    public boolean containsToken(UUID tokenID) {
        return tokens.containsKey(tokenID);
    }


}
