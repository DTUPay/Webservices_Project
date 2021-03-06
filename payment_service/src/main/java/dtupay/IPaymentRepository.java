package dtupay;

import models.Payment;

import java.util.List;
import java.util.UUID;

/**
 * @author Mikkel Rosenfeldt Anderson & Laura & Benjamin
 */
public interface IPaymentRepository {

    void addPayment(Payment payment);

    Payment getPayment(UUID paymentID);

    List<Payment> getPayments();

}
