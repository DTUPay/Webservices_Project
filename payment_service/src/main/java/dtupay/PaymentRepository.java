package dtupay;

import models.Payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    //TODO still relevant?
    public List<Payment> getPayments() {
        return new ArrayList<>(payments.values());
    }

    @Override
    //TODO still relevant?
    public List<Payment> getPayments(UUID merchantID) {
        return payments.values().stream().filter(payment -> payment.getMerchantID().equals(merchantID)).collect(Collectors.toList());
    }
}
