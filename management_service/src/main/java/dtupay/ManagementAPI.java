/*
@author Oliver O. Nielsen
 */

package dtupay;

import dto.CustomerDTO;
import dto.MerchantDTO;

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

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
            return "Welcome to Management Service!";
        }

    @POST
    @Path("/customer")
    @Consumes(MediaType.APPLICATION_JSON)
    public void registerCustomer(@Suspended AsyncResponse response, CustomerDTO customer) {
        service.registerCustomer(customer, response);
    }

    @DELETE
    @Path("/customer/{customerID}")
    public void removeCustomer(@Suspended AsyncResponse response, @PathParam("customerID") String customerID) {
        service.removeCustomer(customerID, response);
    }

    @POST
    @Path("/merchant")
    @Produces(MediaType.APPLICATION_JSON)
    public void registerMerchant(@Suspended AsyncResponse response, MerchantDTO merchant) {
        service.registerMerchant(merchant, response);
    }

    @DELETE
    @Path("/merchant/{merchantID}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void removeMerchant(@Suspended AsyncResponse response, @PathParam("merchantID") String merchantID) {
        service.removeMerchant(merchantID, response);
    }

    @GET
    @Path("/report")
    @Produces(MediaType.APPLICATION_JSON)
    public String getReport() {
        return "Here is your advanced report";
    }

}

