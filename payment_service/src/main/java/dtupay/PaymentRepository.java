package dtupay;

import models.Payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author Mikkel & Laura & Benjamin
 */
public class PaymentRepository implements IPaymentRepository {

    HashMap<UUID, Payment> payments = new HashMap<>();

    @Override
    public void addPayment(Payment payment) {
        payments.put(payment.getPaymentID(),payment);
    }

    @Override
    public Payment getPayment(UUID paymentID) {
        return payments.get(paymentID);
    }

    @Override
    public List<Payment> getPayments() {
        return new ArrayList<>(payments.values());
    }


}
