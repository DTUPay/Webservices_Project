/*
@author Oliver O. Nielsen & Benjamin
 */

package dtupay;

import dto.CustomerDTO;
import dto.MerchantDTO;
import dto.ReportRequestDTO;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;


@Path("/management_service")
public class ManagementAPI {
    ManagementService service = ManagementService.getInstance();

    @GET
    @Path("/debug")
    @Produces(MediaType.TEXT_PLAIN)
    public String debug() {
        return "Welcome to Management Service!";
    }

    @POST
    @Path("/customer")
    @Consumes(MediaType.APPLICATION_JSON)
    public void registerCustomer(@Suspended AsyncResponse response, CustomerDTO customer) {
        service.broker.registerCustomer(customer, response);
    }

    @DELETE
    @Path("/customer/{customerID}")
    public void removeCustomer(@Suspended AsyncResponse response, @PathParam("customerID") String customerID) {
        service.broker.removeCustomer(customerID, response);
    }

    @POST
    @Path("/merchant")
    @Produces(MediaType.APPLICATION_JSON)
    public void registerMerchant(@Suspended AsyncResponse response, MerchantDTO merchant) {
        service.broker.registerMerchant(merchant, response);
    }

    @DELETE
    @Path("/merchant/{merchantID}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void removeMerchant(@Suspended AsyncResponse response, @PathParam("merchantID") String merchantID) {
        service.broker.removeMerchant(merchantID, response);
    }

    @POST
    @Path("/report")
    @Produces(MediaType.APPLICATION_JSON)
    public void requestReport(@Suspended AsyncResponse response) {
        service.broker.requestReport(new ReportRequestDTO(), response);
    }
}

