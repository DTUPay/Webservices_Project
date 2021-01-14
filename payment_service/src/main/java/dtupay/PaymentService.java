package dtupay;

import io.quarkus.runtime.Quarkus;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

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

    public void demo(JsonObject jsonObject){
        // Implement me
    }
}
