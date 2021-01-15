/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen
 */

package dtupay;


import com.google.gson.Gson;
import com.google.gson.JsonParser;
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
            String serviceName = System.getenv("SERVICE_NAME"); //token_service
            System.out.println(serviceName + " started");
            this.rabbitMq = new RabbitMq(serviceName, this);
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


    public void addTokens(JsonObject jsonObject){
        String payloadString = jsonObject.get("payload").toString();
        payloadString = payloadString.substring( 1, payloadString.length() - 1 ).replaceAll("\\\\", "");
        System.out.println(payloadString);
        com.google.gson.JsonObject payload = new JsonParser().parse(payloadString).getAsJsonObject();
        JsonObject callback = (JsonObject) jsonObject.get("callback");
        String callbackService = callback.get("service").toString().replaceAll("\"", "");
        String callbackEvent = callback.get("event").toString().replaceAll("\"", "");

        //Call actual function with data from
        List<UUID> tokenIds = addTokens(
                payload.get("customerId").toString().replaceAll("\"", ""),
                Integer.parseInt(payload.get("amount").toString().replaceAll("\"", ""))
        );

        //Create payload JSON
        JsonObject responsePayload = Json.createObjectBuilder()
                .add("tokenIds", new Gson().toJson(tokenIds)).build();

        String uuid = jsonObject.get("requestId").toString();
        JsonObject response = Json.createObjectBuilder()
                .add("requestId", uuid)
                .add("messageId", UUID.randomUUID().toString())
                .add("event", callbackEvent)
                .add("payload", responsePayload)
                .build();

        System.out.println("Response created");
        rabbitMq.sendMessage(callbackService, response, null);

    }
        //rabbitMqTest.sendMessage("customer_service", response);

    public void demo(JsonObject jsonObject){
        // Implement me
    }




}
