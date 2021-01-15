/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen
 */

package dtupay;

import brokers.RabbitMQ;
import com.google.gson.Gson;
import com.rabbitmq.client.DeliverCallback;
import dto.CustomerServiceDTO;
import models.Callback;
import models.Message;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.io.StringReader;

public class ReportingService {
    RestResponseHandler responseHandler = new RestResponseHandler();
    RabbitMQ broker;
    Gson gson = new Gson();
    DeliverCallback deliverCallback;
    String queue = "reporting_service";

    public ReportingService() {
        try {
            this.broker = new RabbitMQ(queue);
            this.listenOnQueue(queue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getReportByMerchant(String merchantId, AsyncResponse response) throws Exception {

        Message message = new Message();
        CustomerServiceDTO csDTO = new CustomerServiceDTO();

        csDTO.setMerchantId(merchantId);

        message.setPayload(csDTO);
        message.setCallback(new Callback("reporting_service", "getReportByMerchantResponse"));

        System.out.println("Request key:" + message.getMessageId());
        this.sendMessage(queue, message, response);

    }

    public void getReportByMerchantResponse(Message message, JsonObject payload){

        message.setPayload(gson.fromJson(payload.toString(), CustomerServiceDTO.class));

        System.out.println("Received message with key: " + message.getMessageId());
        if(responseHandler.containsRestResponseObject(message.getMessageId())){
            responseHandler.getRestResponseObject(message.getMessageId())
                    .resume(
                            Response.status(200)
                                    .entity(gson.toJson(message))
                                    .build()
                    );

            responseHandler.removeRestResponseObject(message.getMessageId());
        }

        System.out.println("Size of pending requests: " + responseHandler.getSizeOfPendingRequests());
        System.out.println("");

    }

    private void sendMessage(String queue, Message message) throws Exception {
        try{
            broker.sendMessage(queue, gson.toJson(message));
        } catch(Exception e){
            throw new Exception(e);
        }
    }


    private void sendMessage(String queue, Message message, AsyncResponse response) throws Exception {
        responseHandler.saveRestResponseObject(message.getMessageId(), response);

        try{
            broker.sendMessage(queue, gson.toJson(message));
        } catch(Exception e){
            throw new Exception(e);
        }
    }


    private void processMessage(Message message, JsonObject payload){

        switch(message.getEvent()) {
            case "getReportByMerchantResponse":
                this.getReportByMerchantResponse(message, payload);
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
            //String event = jsonObject.get("callback").asJsonObject().get("event").toString().replace("\"", "");

            this.processMessage(gson.fromJson(jsonObject.toString(), Message.class), jsonObject.getJsonObject("payload"));
        };

        this.broker.onQueue(queue, deliverCallback);

    }


}
