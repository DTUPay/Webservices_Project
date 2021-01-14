package dtupay;

import exceptions.PaymentException;
import models.Payment;
import models.PaymentStatus;
import models.Token;

import java.util.List;
import java.util.UUID;

public interface IPaymentRepository {
    Payment getPayment(UUID PaymentID) throws PaymentException;

    UUID requestPayment(int amount, int merchantID);

    PaymentStatus acceptPayment(UUID PaymentID, Token token);

    List<Payment> getManagerSummary();

}
