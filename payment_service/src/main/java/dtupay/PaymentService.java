package dtupay;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/payment_service")
public class PaymentService {
    IPaymentRepository paymentRepository;

    public PaymentService() {this.paymentRepository = new PaymentRepository(); }

    @Path("/request_payment")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public UUID requestPayment(@PathParam("amount") int amount, @PathParam("merchantId") int merchantId) {
        return paymentRepository.requestPayment(amount, merchantId);
    }

    @Path("/test")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "Hello";
    }
}
