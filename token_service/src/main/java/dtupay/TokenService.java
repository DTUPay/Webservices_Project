/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen
 */

package dtupay;


import exceptions.TokenException;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TokenService {
    ITokenRepository tokenRepository;
    RabbitMqTest rabbitMqTest;

    public TokenService() {
        try {
            System.out.println("Token service started");
            this.rabbitMqTest = new RabbitMqTest("token_service", this);
        } catch (Exception e) { e.printStackTrace(); }
        this.tokenRepository = new TokenRepository();
    }

    public ArrayList<UUID> addTokens(int customerID, int amount)  {
        return this.tokenRepository.addTokens(customerID, amount);
    }

    public void addTokens(JsonObject jsonObject){
        JsonObject j = (JsonObject) jsonObject.get("payload");
        System.out.println("Next json parse success");
        List<UUID> tokenIds = addTokens(
                Integer.parseInt(j.get("customerId").toString()),
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
        //rabbitMqTest.sendMessage("customer_service", response);

    }

    public void useToken(UUID tokenID) throws TokenException {
        this.tokenRepository.useToken(tokenID);
    }

    public boolean isTokenValid(UUID tokenID, int customerID) throws TokenException  {
        return this.tokenRepository.isTokenValid(tokenID, customerID);
    }

    public void processMessage(JsonObject jsonObject){
        String action = jsonObject.get("action").toString()
                .replaceAll("\"", "");
        System.out.println("Action of message gotten by token_Service: " + action);
        switch (action){
            case "addTokens":
                System.out.println("Action equals getTokens");
                addTokens(jsonObject);
                break;
        }
    }


    //Calls from RabbitMQ


}
