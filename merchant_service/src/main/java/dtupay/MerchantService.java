package dtupay;

import brokers.MerchantBroker;
import com.google.gson.Gson;
import dto.MerchantIDDTO;
import dto.PaymentDTO;
import exceptions.MerchantException;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import models.Callback;
import models.Merchant;
import models.Message;

import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.util.UUID;

@QuarkusMain
public class MerchantService {
    MerchantBroker broker;
    IMerchantRepository merchantRepository = MerchantRepository.getInstance();
    RestResponseHandler RestfulHandler = RestResponseHandler.getInstance();
    private static MerchantService instance = new MerchantService();
    Gson gson = new Gson();

    public MerchantService() {
        broker = new MerchantBroker(this);
    }
    public static MerchantService getInstance(){
        return instance;
    }

    public static void main(String[] args) {
        MerchantService service = MerchantService.getInstance();
        Quarkus.run();
    }

    public void registerMerchant(Merchant merchant) throws MerchantException {
        if (!merchantRepository.hasMerchant(merchant.getCVR())) {
            merchantRepository.addMerchant(merchant);
        }
        else throw new MerchantException("Merchant with CVR: " + merchant.getCVR() + " already exists");
    }

    // @Status: Implemented
    public void registerMerchant(Message message, JsonObject payload){
        Message reply = broker.createReply(message);
        try{
            Merchant dto = gson.fromJson(payload.toString(), Merchant.class);
            registerMerchant(dto);
        } catch (Exception e) {

            reply.setStatus(400);
            reply.setStatusMessage(e.toString());
            broker.sendMessage(reply);
            return;

        }
        broker.sendMessage(reply);
    }

    // @Status: Implemented
    public void removeMerchant(String cvr) throws MerchantException {
        System.out.println(cvr);
        if (merchantRepository.hasMerchant(cvr)) {
            merchantRepository.removeMerchant(cvr);
        }
        else throw new MerchantException("Merchant with CVR: " + cvr + " doesn't exist");
    }

    // @Status: Implemented
    public void removeMerchant(Message message, JsonObject payload){
        Message reply = broker.createReply(message);
        try{
            MerchantIDDTO dto = gson.fromJson(payload.toString(), MerchantIDDTO.class);
            System.out.println(dto.getMerchantID());
            removeMerchant(dto.getMerchantID());
        } catch (Exception e) {
            reply.setStatus(400);
            reply.setStatusMessage(e.toString());
            broker.sendMessage(reply);
            return;
        }
        broker.sendMessage(reply);
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

    // @Status: Implemented
    // Request payment functions
    public void requestPayment(PaymentDTO payment, AsyncResponse response){
        System.out.println(payment.getMerchantID());
        if(!this.merchantRepository.hasMerchant(payment.getMerchantID())){
            response.resume(Response.status(400).entity("Merchant does not exist").build());
            return;
        }

        Message message = new Message();
        message.setEvent("requestPayment");
        message.setService("payment_service");
        message.setPayload(payment);
        UUID requestId = RestfulHandler.saveRestResponseObject(response);
        message.setRequestId(requestId);

        this.broker.sendMessage(message);
    }

    // Request payment functions
    public void requestPaymentResponse(Message message){
        AsyncResponse response = RestfulHandler.getRestResponseObject(message.getRequestId());
        response.resume(Response.status(message.getStatus()).entity(message.getStatusMessage()).build());
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
