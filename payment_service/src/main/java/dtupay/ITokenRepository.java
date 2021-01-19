package dtupay;

import dto.TokenDTO;
import models.Payment;

import java.util.List;
import java.util.UUID;

/**
 * @author Mikkel Rosenfeldt Anderson & Laura & Benjamin
 */
public interface ITokenRepository {

    UUID saveTokenObject(UUID requestID, TokenDTO token);

    TokenDTO getTokenObject(UUID tokenID);

    void removeTokenObject(UUID tokenId);

}
