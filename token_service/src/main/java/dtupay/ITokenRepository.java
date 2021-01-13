package dtupay;

import exceptions.TokenException;

import java.util.ArrayList;
import java.util.UUID;

public interface ITokenRepository {

    ArrayList<UUID> addTokens(int customerID, int amount);

    void useToken(UUID tokenID) throws TokenException;

    boolean isTokenValid(UUID tokenID, int customerID) throws TokenException;

}
