package dtupay;

import dtu.ws.fastmoney.*;
import exceptions.PaymentException;
import models.Customer;
import models.Payment;
import models.PaymentStatus;
import models.Token;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author Mikkel & Laura
 */
public class PaymentRepository implements IPaymentRepository {

    HashMap<UUID, Payment> payments = new HashMap<>();
    BankService bankService = new BankServiceService().getBankServicePort();

    @Override
    public Payment getPayment(UUID PaymentID) throws PaymentException {
        Payment payment = payments.get(PaymentID);
        if (payment == null) {
            throw new PaymentException("Payment with ID: " + PaymentID + " not found.");
        }
        return payment;
    }

    @Override
    public UUID requestPayment(int amount, int merchantID) throws PaymentException {
        if (amount < 0) throw new PaymentException("Amount is negative");
        if (merchantID < 0) throw new PaymentException("Merchant ID is negative");
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

    @Override
    public String createAccountWithBalance(Customer customer, int amount) throws PaymentException {
        User user = customer.customerToUser();
        String accountNumber;
        try {
            accountNumber = bankService.createAccountWithBalance(user, BigDecimal.valueOf(amount));
        } catch (BankServiceException_Exception e) {
            throw new PaymentException(e.getMessage());
        }
        return accountNumber;
    }

    @Override
    public Account getAccount(String accountNumber) {
        Account account = new Account();
        try {
            account = bankService.getAccount(accountNumber);
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }

        return account;
    }

    @Override
    public void deleteAccount(String cpr) throws PaymentException {
        try {
            Account account = bankService.getAccountByCprNumber(cpr);
            bankService.retireAccount(account.getId());
        } catch (BankServiceException_Exception e) {
            throw new PaymentException(e.getMessage());
        }
    }
}
