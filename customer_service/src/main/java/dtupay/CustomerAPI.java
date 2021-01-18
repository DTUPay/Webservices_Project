/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */

package dtupay;
// @author Rubatharisan & Oliver

import dto.*;

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
    public void requestRefund(@Suspended AsyncResponse response, RefundDTO refund) {
        service.broker.requestRefund(refund, response);
    }

    // @Status: Implemented
    @POST
    @Path("/tokens")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void requestTokens(@Suspended AsyncResponse response, TokensDTO token) {
        service.broker.requestTokens(token, response);
    }

    @POST
    @Path("/report")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void requestReport(@Suspended AsyncResponse response, ReportRequestDTO reportRequest) {
        service.broker.requestReport(reportRequest, response);
    }

}
