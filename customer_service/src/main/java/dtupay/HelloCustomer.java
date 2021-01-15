/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */

package dtupay;


import dto.ReceiveTokensDTO;
import models.Message;
import models.Payload;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;


@Path("/customer_service")
public class HelloCustomer {
    CustomerService service = new CustomerService();


    public HelloCustomer() throws IOException, TimeoutException {

    }

    // DEBUG METHOD
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void hello(@Suspended AsyncResponse response) throws Exception {
        Message message = new Message();

        // Setup DTO
        ReceiveTokensDTO myPayload = new ReceiveTokensDTO();
        List<UUID> uuidList = new ArrayList<>();
        uuidList.add(UUID.randomUUID());
        uuidList.add(UUID.randomUUID());
        uuidList.add(UUID.randomUUID());
        uuidList.add(UUID.randomUUID());

        myPayload.setTokens(uuidList);

        // Setup message properties such as event, callback and whom to respond back to.
        message.setPayload(myPayload);
        message.setEvent("receiveTokens");
        message.setService("customer_service");

        service.testReceiveTokens(message, response);

        /* NEEDS FIX
        UUID uuid = service.addPendingRequest(response);
        JsonObject payload = Json.createObjectBuilder()
                .add("customerId", 1234123)
                .add("amount", 3)
                .build();
        JsonObject message = Json.createObjectBuilder()
                .add("event", "demo")
                .add("payload", payload)
                .add("requestId", uuid.toString())
                .build();

        //service.rabbitMq.sendMessage("token_service", new Message());

         */

    }

    // ACTUAL METHODS
    @POST
    @Path("/Payment/{id}/")
    @Produces(MediaType.TEXT_PLAIN)
    public String acceptPayment(@PathParam("id") int paymentId) {
        // Get payment with id
        // Ensure payment is valid
        // Ensure token is valid
        // Ensure user has funds
        // Transfer money from user to merchant
        // Mark payment as paid
        return "Welcome to Customer Service!";
    }

    @PUT
    @Path("/Payment/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String requestRefund(@PathParam("id") int paymentId) {

        return "Welcome to Customer Service!";
    }

    @POST
    @Path("/Tokens")
    @Produces(MediaType.APPLICATION_JSON)
    public void requestTokens(int tokenAmount, @Suspended AsyncResponse response) {
        /*
        NEEDS FIX
         */
        /*
        UUID uuid = service.addPendingRequest(response);
        Message message = new Message(service.rabbitMq);
        message.setRequestId(uuid);
        message.setEvent("addTokens");
        message.getCallback().setEvent("requestTokens");
        message.setPayload(
                 Json.createObjectBuilder()
                        .add("customerId", 1234123)
                        .add("amount", 3)
                        .build().toString()
        );

        service.rabbitMq.sendMessage("token_service", message);

         */
    }
}
