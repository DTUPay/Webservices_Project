/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */

package dtupay;
// @author Rubatharisan Thirumathyam & Oliver O. Nielsen

import dto.*;
import models.Customer;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/customer_service")
public class CustomerAPI {
    CustomerService service = CustomerService.getInstance();

    @POST
    @Path("/customer")
    @Consumes(MediaType.APPLICATION_JSON)
    //@Produces(MediaType.APPLICATION_JSON)
    public void registerCustomer(@Suspended AsyncResponse response, CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setAccountID(customerDTO.getAccountNumber());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        try {
            service.registerCustomer(customer);
            response.resume(Response.status(201).entity(customer).build());
        } catch(Exception e){
            response.resume(Response.status(400).entity(e.getMessage()).build());
        }
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
