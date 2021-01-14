/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen
 */

package dtupay;


import exceptions.TokenException;
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

    public static void main(String[] args) {
        TokenService service = new TokenService();
        Quarkus.run();
    }

    public ArrayList<UUID> addTokens(int customerID, int amount)  {
        return this.tokenRepository.addTokens(customerID, amount);
    }

    private void addTokens(JsonObject jsonObject){
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
        rabbitMq.sendMessage("customer_service", response);

    }

    public void useToken(UUID tokenID) throws TokenException {
        this.tokenRepository.useToken(tokenID);
    }

    public boolean isTokenValid(UUID tokenID, int customerID) throws TokenException  {
        return this.tokenRepository.isTokenValid(tokenID, customerID);
    }

    public void processMessage(JsonObject jsonObject){
        String event = jsonObject.get("event").toString()
                .replaceAll("\"", "");
        System.out.println("Event of message gotten by token_Service: " + event);
        switch (event){
            case "addTokens":
                System.out.println("Event equals addToken");
                addTokens(jsonObject);
                break;
        }
    }

    public void demo(JsonObject jsonObject){
        // Implement me
    }


    //Calls from RabbitMQ


}
