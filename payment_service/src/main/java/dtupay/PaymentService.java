package dtupay;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.json.JsonObject;

/**
 * @author Mikkel & Laura
 */
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
