package dtupay;
// @author: Oliver O. Nielsen
import dto.TokenDTO;
import models.Message;
import models.Token;

import javax.ws.rs.container.AsyncResponse;
import java.util.HashMap;
import java.util.UUID;

public class TokenRepository {
    public HashMap<UUID, TokenDTO> tokens = new HashMap<>();
    private static TokenRepository instance = new TokenRepository();

    private TokenRepository(){}

    //Get the only object available
    public static TokenRepository getInstance(){
        return instance;
    }

    //TODO
    public boolean containsToken(UUID tokenId){
        return this.tokens.containsKey(tokenId);
    }

    public TokenDTO getMessageObject(UUID tokenId){
        return this.tokens.get(tokenId);
    }

    public void removeMessageObject(UUID tokenId){
        tokens.remove(tokenId);
    }

    public UUID saveTokenObject(UUID requestID, TokenDTO token){
        UUID uuid = requestID;
        tokens.put(uuid, token);
        return uuid;
    }

    public Integer getSizeOfMessages() {
        return this.tokens.size();
    }
}
