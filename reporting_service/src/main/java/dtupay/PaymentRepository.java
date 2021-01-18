package dtupay;

import models.Payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author Mikkel Rosenfeldt Anderson & Laura & Benjamin
 */
public class PaymentRepository implements IPaymentRepository {

    HashMap<UUID, Payment> payments = new HashMap<>();
    private static PaymentRepository instance = new PaymentRepository();
    public static PaymentRepository getInstance(){
        return instance;
    }

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

    public void removeAllPayments(){
        payments.clear();
        /*Iterator it = payments.entrySet().iterator();
        while (it.hasNext()) {
            it.remove(); // avoids a ConcurrentModificationException
        }*/
    }
}
