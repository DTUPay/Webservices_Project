/*
@author Oliver O. Nielsen
 */

package dtupay;


import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;


@Path("/customer_service")
public class HelloCustomer {

    CustomerService service = new CustomerService();
    // DEBUG METHOD
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void hello(@Suspended AsyncResponse response) {

        System.out.println("Adding pending request");
        UUID uuid = service.addPendingRequest(response);
        System.out.println("UUID customer_service side: " + uuid.toString());
        System.out.println("UUID length customer_serice side: " + uuid.toString().length());
        JsonObject payload = Json.createObjectBuilder()
                .add("customerId", 1234123)
                .add("amount", 3)
                .build();
        JsonObject message = Json.createObjectBuilder()
                .add("action", "addTokens")
                .add("payload", payload)
                .add("requestId", uuid.toString())
                .build();

        try {
            System.out.println("Sending a message to token_service");
            RabbitMqTest rabbitMqTest = new RabbitMqTest("customer_service", service);
            rabbitMqTest.sendMessage("token_service", message);
        } catch (Exception e) {
            System.out.println(e);
        }
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
    public String requestTokens(int tokenAmount) {
        return "Welcome to Customer Service!";
    }
}
