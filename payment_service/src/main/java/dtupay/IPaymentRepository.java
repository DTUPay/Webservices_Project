package dtupay;

import dtu.ws.fastmoney.Account;
import exceptions.PaymentException;
import models.Customer;
import models.Payment;
import models.PaymentStatus;
import models.Token;

import java.util.List;
import java.util.UUID;

/**
 * @author Mikkel & Laura
 */
public interface IPaymentRepository {
    Payment getPayment(UUID PaymentID) throws PaymentException;

    UUID requestPayment(int amount, int merchantID) throws PaymentException;

    PaymentStatus acceptPayment(UUID PaymentID, Token token);

    List<Payment> getManagerSummary();

    String createAccountWithBalance(Customer customer, int amount) throws PaymentException;

    Account getAccount(String accountNumber);

    void deleteAccount(String cpr) throws PaymentException;
}
