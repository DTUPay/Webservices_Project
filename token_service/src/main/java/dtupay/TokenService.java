/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen
 */

package dtupay;


import brokers.TokenBroker;
import com.google.gson.Gson;
import com.rabbitmq.client.DeliverCallback;
import exceptions.TokenException;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import models.Token;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen & Mikkel Rosenfeldt Anderson
 */
@QuarkusMain
public class TokenService {
    ITokenRepository tokenRepository = TokenRepository.getInstance();
    TokenBroker broker;
    Gson gson = new Gson();
    DeliverCallback deliverCallback;
    String queue = "token_service";
    private static TokenService instance = new TokenService();
    public static TokenService getInstance(){
        return instance;
    }

    public TokenService() {
        broker = new TokenBroker(this);
    }


    public static void main(String[] args) {
        TokenService service = TokenService.getInstance();
        Quarkus.run();
    }


    /*
        TokenService Functionality
     */

    public ArrayList<UUID> addTokens(String customerID, int amount) {
        ArrayList<UUID> tokenIDs = new ArrayList<>();
        for (int i = 0; i< amount; i++) {
            Token token = new Token(customerID);
            tokenRepository.addToken(token);
            tokenIDs.add(token.getTokenID());
        }
        return tokenIDs;
    }


    public boolean useToken(UUID tokenID) throws TokenException {
        if (tokenRepository.containsToken(tokenID)) {
            if (!tokenRepository.getToken(tokenID).isUsed()){
                tokenRepository.getToken(tokenID).setUsed(true);
                return true;
            } else {
                throw new TokenException("Token has already been used");
            }
        }
        throw new TokenException("Token doesn't exist");
    }

    public Token getToken(UUID tokenID) throws TokenException {
        if (tokenRepository.containsToken(tokenID)) {
            return tokenRepository.getToken(tokenID);
        }
        throw new TokenException("Token doesn't exist");
    }

    public String isTokenValid(UUID tokenID) throws TokenException {
        if (this.tokenRepository.containsToken(tokenID)) {
            if (!this.tokenRepository.getToken(tokenID).isUsed()) {
                return this.tokenRepository.getToken(tokenID).getCustomerID();
            }
            else {
                throw new TokenException("Token has already been used");
            }
        }
        throw new TokenException("Token doesn't exist");
    }



}
