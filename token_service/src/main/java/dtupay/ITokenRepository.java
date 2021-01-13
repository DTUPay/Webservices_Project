package dtupay;

import java.util.UUID;

public interface ITokenRepository {

    void addTokens(String customerID, int amount) throws Exception;

    void useToken(UUID tokenID) throws Exception;


}
