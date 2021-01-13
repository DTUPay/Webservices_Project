package dtupay;

import models.Customer;
import models.Payment;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Mikkel
 * @reviewer Laura
 */
public class PaymentRepository implements IPaymentRepository {

    HashMap<UUID, Payment> payments = new HashMap<>();

    @Override
    public Payment getPayment(UUID PaymentID) throws Exception {
        return payments.get(PaymentID);
    }

    @Override
    public UUID requestPayment(int amount, int merchantID) throws Exception {
        UUID uuid = UUID.randomUUID();
        Payment newPayment = new Payment(amount,merchantID);
        newPayment.setPaymentID(uuid);
        payments.put(uuid, newPayment);
        return uuid;
    }
}
