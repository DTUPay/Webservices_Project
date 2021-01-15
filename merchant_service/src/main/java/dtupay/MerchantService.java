package dtupay;

import brokers.MerchantBroker;
import com.google.gson.Gson;
import dto.MerchantIDDTO;
import dto.PaymentDTO;
import dto.TokenIDDTO;
import exceptions.MerchantException;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import models.Callback;
import models.Merchant;
import models.Message;

import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.UUID;

@QuarkusMain
public class MerchantService {
    MerchantBroker broker;
    public HashMap<UUID, AsyncResponse> pendingRequests = new HashMap<>();
    IMerchantRepository merchantRepository;
    RestResponseHandler RestfulHandler = RestResponseHandler.getInstance();
    PayloadHandler payloadHandler = PayloadHandler.getInstance();
    Gson gson = new Gson();

    public MerchantService() {
        try {
            if(System.getenv("ENVIRONMENT") != null){
                this.broker = new MerchantBroker(this);
            }
        } catch (Exception e) { e.printStackTrace(); }
        this.merchantRepository = new MerchantRepository();
    }

    public static void main(String[] args) {
        MerchantService service = new MerchantService();
        Quarkus.run();
    }

    public UUID addPendingRequest(AsyncResponse asyncResponse){
        UUID uuid = UUID.randomUUID();
        pendingRequests.put(uuid, asyncResponse);
        return uuid;
    }

    public void respondPendingRequest(UUID uuid){
        pendingRequests.get(uuid).resume(Response.status(202).build());
        pendingRequests.remove(uuid);
    }

    public void demo(JsonObject jsonObject){
        // UUID needs to be trimmed after convertion from JSON
        String uuidString = jsonObject.get("uuid").toString().replaceAll("\"", "").replaceAll("\\\\", "");
        UUID uuid = UUID.fromString(uuidString);
        respondPendingRequest(uuid);
    }

    public void registerMerchant(Merchant merchant) throws MerchantException {
        if (!merchantRepository.hasMerchant(merchant.getCVR())) {
            merchantRepository.addMerchant(merchant);
        }
        else throw new MerchantException("Merchant with CVR: " + merchant.getCVR() + " already exists");
    }

    //Fire and forget for now
    public void registerMerchant(Message message, JsonObject payload){
        Message reply = broker.createReply(message);
        try{
            Merchant dto = gson.fromJson(payload.toString(), Merchant.class);
            registerMerchant(dto);
        } catch (Exception e) {

            /*reply.setStatus(400);
            broker.sendMessage(reply);
            return;*/
        }
        //broker.sendMessage(reply);
    }

    public void removeMerchant(String cvr) throws MerchantException {

        if (merchantRepository.hasMerchant(cvr)) {
            merchantRepository.removeMerchant(cvr);
        }
        else throw new MerchantException("Merchant with CVR: " + cvr + " doesn't exist");
    }

    public void removeMerchant(Message message, JsonObject payload){
        Message reply = broker.createReply(message);
        try{
            MerchantIDDTO dto = gson.fromJson(payload.toString(), MerchantIDDTO.class);
            removeMerchant(dto.getMerchantID());
        } catch (Exception e) {

            /*reply.setStatus(400);
            broker.sendMessage(reply);
            return;*/
        }
        //broker.sendMessage(reply);
    }

    public Merchant getMerchant(String cvr) throws MerchantException {
        if (merchantRepository.hasMerchant(cvr)) {
            return merchantRepository.getMerchant(cvr);
        }
        else throw new MerchantException("Merchant with CVR: " + cvr + " doesn't exist");
    }

    public void getMerchant(Message message, JsonObject payload){
        Message reply = broker.createReply(message);
        try{
            MerchantIDDTO dto = gson.fromJson(payload.toString(), MerchantIDDTO.class);
            Merchant merchant = getMerchant(dto.getMerchantID());
            reply.payload = merchant;
        } catch (Exception e) {
            reply.setStatus(400);
            broker.sendMessage(reply);
            return;
        }
        broker.sendMessage(reply);
    }

    // Request payment functions
    public void requestPayment(PaymentDTO payment, AsyncResponse response){
        requestPaymentValidateToken(payment, response);
    }

    public void requestPaymentValidateToken(PaymentDTO payment, AsyncResponse response){
        UUID requestId = RestfulHandler.saveRestResponseObject(response);
        //Add payload to local memory
        payloadHandler.savePayloadsObject(requestId, payment);
        Message message = new Message();
        message.setEvent("isTokenValid");
        message.setService("token_service");
        message.setRequestId(requestId);

        TokenIDDTO payload = new TokenIDDTO();
        payload.setTokenID(payment.getTokenID());
        message.setPayload(payload);
        message.setCallback(new Callback("merchant_service", "requestPaymentTokenValidation"));
        broker.sendMessage(message);
    }

    public void requestPaymentAtPaymentService(Message messageFromTokenService, JsonObject payload){
        //If token is not valid, do not continue
        if(messageFromTokenService.getStatus() != 200){
            AsyncResponse request = RestfulHandler.getRestResponseObject(messageFromTokenService.getRequestId());
            RestfulHandler.removeRestResponseObject(messageFromTokenService.getRequestId());
            request.resume(Response.status(400));
            return;
        }

        //If token is valid, send message to payment service
        Message message = new Message();
        message.setEvent("requestPayment");
        message.setService("payment_service");
        message.setRequestId(messageFromTokenService.getRequestId());

        //TODO set payload according to merchangService interface when defined
        //Get and remove payload from local memory
        PaymentDTO payloadDTO = (PaymentDTO) payloadHandler.getPayloadsObject(message.getRequestId());
        payloadHandler.removePayloadsObject(message.getRequestId());
        message.setPayload(payloadDTO);

        message.setCallback(new Callback("merchant_service", "requestPaymentComplete"));
        broker.sendMessage(message);
    }

    public void requestPaymentComplete(Message message, JsonObject payload) {
        //Return status of rabbitMQ request
        AsyncResponse request = RestfulHandler.getRestResponseObject(message.getRequestId());
        RestfulHandler.removeRestResponseObject(message.getRequestId());
        request.resume(Response.status(message.getStatus()));
    }


    /*
    Generate report functions
     */
    public void generateReport(PaymentDTO reportRequestDTO, AsyncResponse response){
        UUID requestId = RestfulHandler.saveRestResponseObject(response);

        Message message = new Message();
        message.setEvent("getMerchantSummary");
        message.setService("payment_service");
        message.setRequestId(requestId);

        message.setPayload(reportRequestDTO);
        message.setCallback(new Callback("merchant_service", "returnMerchantSummary"));
        broker.sendMessage(message);
    }

    public void returnMerchantReport(Message message, JsonObject payload) {
        AsyncResponse request = RestfulHandler.getRestResponseObject(message.getRequestId());
        RestfulHandler.removeRestResponseObject(message.getRequestId());
        if(message.getStatus() != 200){
            request.resume(Response.status(message.getStatus()));
            return;
        }

        //TODO cast payload to expected DTO before returning
        request.resume(Response.status(message.getStatus()).entity(payload));
    }
}
