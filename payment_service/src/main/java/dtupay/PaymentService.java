package dtupay;

import javax.ws.rs.*;
import io.quarkus.runtime.Quarkus;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/payment_service")
public class PaymentService {
    IPaymentRepository paymentRepository;
    RabbitMq rabbitMq;

    public PaymentService() {
        try {
            System.out.println("Token service started");
            this.rabbitMq = new RabbitMq("token_service", this);
        } catch (Exception e) { e.printStackTrace(); }
        this.paymentRepository = new PaymentRepository();
    }

    public static void main(String[] args) {
        PaymentService service = new PaymentService();
        Quarkus.run();
    }

    @Path("/request_payment")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public UUID requestPayment(@PathParam("amount") int amount, @PathParam("merchantId") int merchantId) {
        return paymentRepository.requestPayment(amount, merchantId);
    }
    public void demo(JsonObject jsonObject){
        // Implement me
    }

    @Path("/test")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "Hello";
    }
}
