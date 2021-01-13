package dtupay;

import models.Payment;

import java.util.UUID;

public interface IPaymentRepository {
    Payment getPayment(UUID PaymentID);

    UUID requestPayment(int amount, int merchantID);

}
