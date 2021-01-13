/*
@author Oliver O. Nielsen
 */

package dtupay;

import models.Merchant;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("/management_service")
public class HelloManagement {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
            return "Welcome to Management Service!";
        }

    @POST
    @Path("/Customer")
    @Consumes(MediaType.APPLICATION_JSON)
    public String addCustomer() {
        return "A customer was added";
    }

    @DELETE
    @Path("/Customer/{id}")
    public String deleteCustomer(@PathParam("id") int customerId) {
        return "A customer was added";
    }

    @POST
    @Path("/Merchant")
    @Produces(MediaType.APPLICATION_JSON)
    public String addMerchant(Merchant merchant) {
        return "A merchant was added";
    }

    @DELETE
    @Path("/Customer/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteMerchant(@PathParam("id") int merchantId) {
        return "A customer was added";
    }

    @GET
    @Path("/Report")
    @Produces(MediaType.APPLICATION_JSON)
    public String getReport() {
        return "Here is your advanced report";
    }

}

