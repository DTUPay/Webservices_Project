package dtupay;

import brokers.ManagementBroker;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import models.Message;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import java.io.StringReader;

@QuarkusMain
public class ManagementService {
    ManagementBroker broker;
    private static ManagementService instance = new ManagementService();

    private ManagementService(){

        try {
            if(System.getenv("ENVIRONMENT") != null){
                this.broker = new RabbitMQ(queue);
                this.listenOnQueue(queue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Get the only object available
    public static ManagementService getInstance(){
        return instance;
    }

    public static void main(String[] args) {
        ManagementService service = ManagementService.getInstance();
        Quarkus.run();
    }


    /*
        Management service methods
     */



    /*
        RabbitMQ call and callback
     */

    private void processMessage(Message message, JsonObject payload){
        switch(message.getEvent()) {
            case "receiveMessage":
                System.out.println(message.toString());
                break;
            default:
                System.out.println("Event not handled: " + message.getEvent());
        }

    }

    private void sendMessage(String queue, Message message) throws Exception {
        try{
            broker.sendMessage(message);
        } catch(Exception e){
            throw new Exception(e);
        }
    }

    private void sendMessage(Message message, AsyncResponse response) throws Exception {
        responseHandler.saveRestResponseObject(response);

        try{
            broker.sendMessage(message);
            System.out.println("Message sent");
        } catch(Exception e){
            throw new Exception(e);
        }
    }

    private void listenOnQueue(String queue){

        deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject(); // @TODO: Validate Message, if it is JSON object

            this.processMessage(gson.fromJson(jsonObject.toString(), Message.class), jsonObject.getJsonObject("payload"));
        };

        this.broker.onQueue(queue, deliverCallback);

    }

}
