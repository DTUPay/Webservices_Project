/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen
 */

package dtupay;


import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.rabbitmq.client.DeliverCallback;
import dto.AddTokensDTO;
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
    RabbitMQ broker;
    io.cucumber.messages.internal.com.google.gson.Gson gson = new io.cucumber.messages.internal.com.google.gson.Gson();
    DeliverCallback deliverCallback;


    public TokenService() {
        try {
            String serviceName = System.getenv("SERVICE_NAME"); //token_service
            System.out.println(serviceName + " started");
            this.broker = new RabbitMQ(serviceName);
            this.listenOnQueue(serviceName);
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

    /*
        RabbitMQ call and callback
     */


    public static void main(String[] args) {
        TokenService service = new TokenService();
        Quarkus.run();
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

    private void processMessage(Message message, JsonObject payload){

        switch(message.getEvent()) {
            case "addTokens":
                System.out.println("Addtoken event caught");
                this.addTokens(message, payload);
                break;
            default:
                System.out.println("Event not handled: " + message.getEvent());
        }

    }

    private void listenOnQueue(String queue){

        deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject(); // @TODO: Validate Message, if it is JSON object
            System.out.println(jsonObject.toString());

            System.out.println("Message received on token side");
            this.processMessage(gson.fromJson(jsonObject.toString(), Message.class), jsonObject.getJsonObject("payload"));
        };

        this.broker.onQueue(queue, deliverCallback);

    }




}
