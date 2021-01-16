/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen
 */

package dtupay;


import com.google.gson.Gson;
import com.rabbitmq.client.DeliverCallback;
import dto.RequestTokensDTO;
import dto.TokenDTO;
import dto.TokenIDDTO;
import dto.TokenIdListDTO;
import exceptions.TokenException;
import models.Message;
import models.Token;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import brokers.*;

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


    public void useToken(Message message, JsonObject payload){
        Message reply = broker.createReply(message);
        TokenIDDTO dto = gson.fromJson(payload.toString(), TokenIDDTO.class);

        try {
            if(this.useToken(dto.getTokenID())){
                TokenDTO tokenDTO = new TokenDTO(getToken(dto.getTokenID()));
                reply.setPayload(tokenDTO);
                this.broker.sendMessage(reply);
            }
        } catch(Exception e){
            reply.setStatus(400);
            reply.setStatusMessage(e.toString());
            this.broker.sendMessage(reply);
        }
    }


    public void requestTokens(Message message, JsonObject payload){

        System.out.println(payload);
        RequestTokensDTO dto = gson.fromJson(payload.toString(), RequestTokensDTO.class);

        //Call actual function with data from
        List<UUID> tokenIds = addTokens(
                dto.getCustomerId(),
                dto.getAmount()
        );

        System.out.println("Tokends added");
        //Create payload JSON
        TokenIdListDTO replyPayload = new TokenIdListDTO();
        replyPayload.setTokenIds(tokenIds);

        Message reply = broker.createReply(message);
        reply.payload = replyPayload;

        System.out.println("Response created");
        broker.sendMessage(reply);

    }
}
