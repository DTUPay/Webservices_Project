package dtupay;


import exceptions.TokenException;

import java.util.ArrayList;
import java.util.UUID;

public class TokenService {
    ITokenRepository tokenRepository;

    public TokenService() {
        this.tokenRepository = new TokenRepository();
    }

    public ArrayList<UUID> addTokens(int customerID, int amount)  {
        return this.tokenRepository.addTokens(customerID, amount);
    }

    public void useToken(UUID tokenID) throws TokenException {
        this.tokenRepository.useToken(tokenID);
    }

    public boolean isTokenValid(UUID tokenID, int customerID) throws TokenException  {
        return this.tokenRepository.isTokenValid(tokenID, customerID);
    }


    //Calls from RabbitMQ


}
