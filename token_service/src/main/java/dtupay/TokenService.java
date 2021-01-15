/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen
 */

package dtupay;


import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.rabbitmq.client.DeliverCallback;
import dto.AddTokensDTO;
import dto.CustomerIDDTO;
import dto.TokenIDDTO;
import dto.TokenIdListDTO;
import exceptions.TokenException;
import models.Message;
import models.Token;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import brokers.*;

@QuarkusMain
public class TokenService {
    ITokenRepository tokenRepository;
    TokenBroker broker;
    Gson gson = new Gson();
    DeliverCallback deliverCallback;
    String queue = "token_service";

    public TokenService() {
        try {
            if(System.getenv("ENVIRONMENT") != null){
                this.broker = new TokenBroker(this);
            }
        } catch (Exception e) { e.printStackTrace(); }
        this.tokenRepository = new TokenRepository();
    }


    public static void main(String[] args) {
        TokenService service = new TokenService();
        Quarkus.run();
    }


    /*
        TokenService Functionality
     */

    public ArrayList<UUID> addTokens(String customerID, int amount) {
        //TODO: Verify amount of tokens! -> Do in CustomerService
        ArrayList<UUID> tokenIDs = new ArrayList<>();
        for (int i = 0; i< amount; i++) {
            Token token = new Token(customerID);
            tokenRepository.addToken(token);
            tokenIDs.add(token.getTokenID());
        }
        return tokenIDs;
    }


    public void useToken(UUID tokenID) throws TokenException {
        if (tokenRepository.containsToken(tokenID)) {
            if (!tokenRepository.getToken(tokenID).isUsed()){
                tokenRepository.getToken(tokenID).setUsed(true);
            } else {
                throw new TokenException("Token has already been used");
            }
        }
        throw new TokenException("Token doesn't exist");
    }

    public void useToken(Message message, JsonObject payload){
        System.out.println(payload);
        Message reply = broker.createReply(message);
        TokenIDDTO dto = gson.fromJson(payload.toString(), TokenIDDTO.class);
        try {
            useToken(dto.getTokenID());
        } catch (TokenException e) {
            e.printStackTrace();
        }
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

    public void isTokenValid(Message message, JsonObject payload){
        System.out.println(payload);
        Message reply = broker.createReply(message);
        TokenIDDTO dto = gson.fromJson(payload.toString(), TokenIDDTO.class);
        try {
            String customerId = isTokenValid(dto.getTokenID());
            CustomerIDDTO replyPayload = new CustomerIDDTO();
            replyPayload.setCustomerID(customerId);
            reply.payload = replyPayload;
        } catch (TokenException e) {
            reply.setStatus(400);
            broker.sendMessage(reply);
            return;
        }

        System.out.println("Sending response");
        broker.sendMessage(reply);
    }


    public void addTokens(Message message, JsonObject payload){

        System.out.println(payload);
        AddTokensDTO dto = gson.fromJson(payload.toString(), AddTokensDTO.class);

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
