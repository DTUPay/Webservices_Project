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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void requestRefund(@Suspended AsyncResponse response, PaymentDTO payment) throws Exception {
        service.broker.requestRefund(payment, response);
    }

    // @Status: Implemented
    @POST
    @Path("/tokens")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void requestTokens(@Suspended AsyncResponse response, TokensDTO token) throws Exception {
        service.broker.requestTokens(token, response);
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

    // @Status: In dispute / in partial
    /*
    @GET
    @Path("/token/{customerID}/amount")
    @Produces(MediaType.APPLICATION_JSON)
    public void getNumberOfTokens(@Suspended AsyncResponse response, @PathParam("customerID") String customerID) throws Exception {
        service.getNumberOfTokens(customerID, response);
    }
    */


}
