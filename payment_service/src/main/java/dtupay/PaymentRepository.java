package dtupay;

import exceptions.PaymentException;
import models.Payment;
import models.PaymentStatus;
import models.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author Mikkel & Laura
 */
public class PaymentRepository implements IPaymentRepository {

    HashMap<UUID, Payment> payments = new HashMap<>();

    @Override
    public Payment getPayment(UUID PaymentID) throws PaymentException {
        Payment payment = payments.get(PaymentID);
        if (payment == null) {
            throw new PaymentException("Payment with ID: " + PaymentID + " not found.");
        }
        return payment;
    }

    @Override
    public UUID requestPayment(int amount, int merchantID) {
        UUID uuid = UUID.randomUUID();
        Payment newPayment = new Payment(amount,merchantID);
        newPayment.setPaymentID(uuid);
        payments.put(uuid, newPayment);
        return uuid;
    }

    @Override
    public PaymentStatus acceptPayment(UUID PaymentID, Token token) {
        /* TODO Communicate with TokenService to validate*/
        return null;
    }

    public List<Payment> getManagerSummary() {
        List<Payment> paymentList = new ArrayList<>();
        for (UUID uuid : payments.keySet()) {
            paymentList.add(payments.get(uuid));
        }
        return paymentList;
    }
}
