package dtupay;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

public class PaymentService {
    IPaymentRepository paymentRepository;

    public PaymentService() {this.paymentRepository = new PaymentRepository(); }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public UUID requestPayment(@PathParam("amount") int amount, @PathParam("merchantId") int merchantId) {
        return paymentRepository.requestPayment(amount, merchantId);
    }
}
