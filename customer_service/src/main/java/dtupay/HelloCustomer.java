package dtupay;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("/customer_service")
public class HelloCustomer {

    // DEBUG METHOD
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Welcome to Customer Service!";
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
