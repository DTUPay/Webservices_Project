/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */

package dtupay;
// @author Rubatharisan & Oliver

import dto.CustomerDTO;
import dto.PaymentDTO;
import dto.TokensDTO;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;


@Path("/customer_service")
public class CustomerAPI {
    CustomerService service = CustomerService.getInstance();

    @POST
    @Path("/customer")
    @Consumes(MediaType.APPLICATION_JSON)
    public void registerCustomer(@Suspended AsyncResponse response, CustomerDTO customer) {
        //service.registerCustomer(customer, response);
    }

    // @Status: Implemented
    @PUT
    @Path("/refund")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void requestRefund(@Suspended AsyncResponse response, PaymentDTO payment) {
        service.broker.requestRefund(payment, response);
    }

    // @Status: Implemented
    @POST
    @Path("/tokens")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void requestTokens(@Suspended AsyncResponse response, TokensDTO token) {
        service.broker.requestTokens(token, response);
    }


}
