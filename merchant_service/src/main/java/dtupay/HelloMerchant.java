package dtupay;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;


@Path("/merchant_service")
public class HelloMerchant {
    MerchantService service = new MerchantService();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Welcome to Merchant Service!";
    }

    /*@GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public UUID requestPayment(@PathParam("amount") int amount, @PathParam("merchantId") int merchantId) {
        //return paymentRepository.requestPayment(amount, merchantId);
        return UUID.randomUUID();
    }*/

}
