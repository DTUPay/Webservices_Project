/*
@author Oliver O. Nielsen
 */

package dtupay;

import models.Merchant;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("/management_service")
public class ManagementAPI {
    ManagementService service = new ManagementService();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
            return "Welcome to Management Service!";
        }

    @POST
    @Path("/customer")
    @Consumes(MediaType.APPLICATION_JSON)
    public String addCustomer() {
        return "A customer was added";
    }

    @DELETE
    @Path("/customer/{id}")
    public String deleteCustomer(@PathParam("id") int customerId) {
        return "A customer was added";
    }

    @POST
    @Path("/merchant")
    @Produces(MediaType.APPLICATION_JSON)
    public String addMerchant(Merchant merchant) {
        return "A merchant was added";
    }

    @DELETE
    @Path("/customer/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteMerchant(@PathParam("id") int merchantId) {
        return "A customer was added";
    }

    @GET
    @Path("/report")
    @Produces(MediaType.APPLICATION_JSON)
    public String getReport() {
        return "Here is your advanced report";
    }

}

