/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */

package dtupay;
/* CODE FREEZE! */

import dto.PaymentDTO;
import dto.TokensDTO;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;


@Path("/customer_service")
public class CustomerAPI {
    CustomerService service = CustomerService.getInstance();

    // @Status: Implemented
    @PUT
    @Path("/refund")
    @Produces(MediaType.APPLICATION_JSON)
    public void requestRefund(@Suspended AsyncResponse response, PaymentDTO payment) throws Exception {
        service.requestRefund(payment, response);
    }

    // @Status: Implemented
    @POST
    @Path("/tokens")
    @Produces(MediaType.APPLICATION_JSON)
    public void requestTokens(@Suspended AsyncResponse response, TokensDTO token) throws Exception {
        service.requestTokens(token, response);
    }

    // @Status: In dispute / in partial
    /*
    @GET
    @Path("/token/{customerID}")
    @Produces(MediaType.APPLICATION_JSON)
    public void getUnusedToken(@Suspended AsyncResponse response, @PathParam("customerID") String customerID) throws Exception {
        service.getUnusedToken(customerID, response);
    }
     */



}
/*


    // ACTUAL METHODS
    @POST
    @Path("/payment/{id}/")
    @Consumes(MediaType.APPLICATION_JSON)
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
    @Path("/payment/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String requestRefund(@PathParam("id") int paymentId) {

        return "Welcome to Customer Service!";
    }

    @POST
    @Path("/tokens")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void requestTokens(int tokenAmount, @Suspended AsyncResponse response) {
/*
        System.out.println("request tokens started");
        UUID uuid = service.responseHandler.saveRestResponseObject(response);
        Message message = new Message("token_service", "addTokens");
        message.setRequestId(uuid);
        message.getCallback().setEvent("receiveTokens");
        message.getCallback().setService("customer_service");

        System.out.println("message created");
        //Create payload
        AddTokensDTO payload = new AddTokensDTO();
        payload.setCustomerId("testId");
        payload.setAmount(tokenAmount);
        message.setPayload(payload);

        service.broker.sendMessage(message);*/
/*
}
*/
