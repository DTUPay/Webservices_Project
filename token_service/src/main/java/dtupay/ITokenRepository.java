/*
@author Benjamin Eriksen
 */

package dtupay;

import models.Token;

import java.util.UUID;

public interface ITokenRepository {

    void addToken(Token token);

    Token getToken(UUID tokenID);

    boolean containsToken(UUID tokenID);
}
