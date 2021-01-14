/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen
 */

package dtupay;


import exceptions.TokenException;
import models.Token;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@QuarkusMain
public class TokenService {
    ITokenRepository tokenRepository;
    RabbitMq rabbitMq;


    public TokenService() {
        try {
            System.out.println("Token service started");
            this.rabbitMq = new RabbitMq("token_service", this);
        } catch (Exception e) { e.printStackTrace(); }
        this.tokenRepository = new TokenRepository();
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

    public boolean isTokenValid(UUID tokenID, String customerID) throws TokenException {
        if (this.tokenRepository.containsToken(tokenID)) {
            return this.tokenRepository.getToken(tokenID).getCustomerID().equals(customerID) && !this.tokenRepository.getToken(tokenID).isUsed();
        }
        throw new TokenException("Token doesn't exist");
    }

    /*
        RabbitMQ call and callback
     */


    public static void main(String[] args) {
        TokenService service = new TokenService();
        Quarkus.run();
    }


    private void addTokens(JsonObject jsonObject){
        JsonObject j = (JsonObject) jsonObject.get("payload");
        System.out.println("Next json parse success");
        List<UUID> tokenIds = addTokens(
                j.get("customerId").toString(),
                Integer.parseInt(j.get("amount").toString())
        );
        System.out.println("Amount and customerID parse success");
        String uuid = jsonObject.get("requestId").toString();
        JsonObject response = Json.createObjectBuilder()
                .add("hello", "hello")
                .add("uuid", uuid)
                //.add("requestId", uuid.toString())
                .build();
        System.out.println("Response created");
        rabbitMq.sendMessage("customer_service", response);

    }
        //rabbitMqTest.sendMessage("customer_service", response);

    public void demo(JsonObject jsonObject){
        // Implement me
    }




}
