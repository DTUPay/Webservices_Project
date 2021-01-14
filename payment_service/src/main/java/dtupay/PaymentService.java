package dtupay;

import exceptions.PaymentException;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import models.Payment;
import models.PaymentStatus;
import models.Token;

import javax.json.JsonObject;
import java.util.List;
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


    public Payment getPayment(UUID PaymentID) throws PaymentException {
        Payment payment = paymentRepository.getPayment(PaymentID);
        if (payment == null) {
            throw new PaymentException("Payment with ID: " + PaymentID + " not found.");
        }
        return payment;
    }

    public UUID requestPayment(int amount, int merchantID) {
        Payment newPayment = new Payment(amount,merchantID);
        paymentRepository.addPayment(newPayment);
        return newPayment.getPaymentID();
    }


    public List<Payment> getManagerSummary() {
        return paymentRepository.getPayments();
    }


    public static void main(String[] args) {
        PaymentService service = new PaymentService();
        Quarkus.run();
    }

    public void demo(JsonObject jsonObject){
        // Implement me
    }

    public PaymentStatus acceptPayment(UUID PaymentID, Token token) {
        /* TODO Communicate with TokenService to validate*/
        return null;
    }
}
