package dtupay;

import javax.ws.rs.*;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@QuarkusMain
public class PaymentService {
    IPaymentRepository paymentRepository;
    RabbitMq rabbitMq;

    public PaymentService() {
        try {
            String serviceName = System.getenv("SERVICE_NAME"); //payment_service
            System.out.println(serviceName + " started");
            this.rabbitMq = new RabbitMq(serviceName, this);
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
