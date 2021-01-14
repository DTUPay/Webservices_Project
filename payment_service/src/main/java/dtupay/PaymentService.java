package dtupay;

import dtu.ws.fastmoney.*;
import exceptions.PaymentException;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import models.Customer;
import models.Payment;
import models.PaymentStatus;
import models.Token;

import javax.json.JsonObject;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @author Mikkel & Laura & Benjamin
 */
@QuarkusMain
public class PaymentService {
    IPaymentRepository paymentRepository;
    BankService bankService = new BankServiceService().getBankServicePort();
    RabbitMq rabbitMq;

    public PaymentService() {
        try {
            String serviceName = System.getenv("SERVICE_NAME"); //payment_service
            System.out.println(serviceName + " started");
            this.rabbitMq = new RabbitMq(serviceName, this);
        } catch (Exception e) { e.printStackTrace(); }
        this.paymentRepository = new PaymentRepository();
    }

    public static void main(String[] args) {
        PaymentService service = new PaymentService();
        Quarkus.run();
    }

    public Payment getPayment(UUID PaymentID) throws PaymentException {
        Payment payment = paymentRepository.getPayment(PaymentID);
        if (payment == null) {
            throw new PaymentException("Payment with ID: " + PaymentID + " not found.");
        }
        return payment;
    }

    public UUID requestPayment(int amount, int merchantID) throws PaymentException{
        if (merchantID < 0) {
            throw new PaymentException("Non positive merchant ID");
        }
        if (amount <= 0) {
            throw new PaymentException("Non positive payment amount");
        }
        Payment newPayment = new Payment(amount,merchantID);
        paymentRepository.addPayment(newPayment);
        return newPayment.getPaymentID();
    }


    public List<Payment> getManagerSummary() {
        return paymentRepository.getPayments();
    }

    /*
     *  Bank Interactions
     */
    //TODO: Bank custom exception for bank interactions?
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

    public Account getAccount(String accountNumber) {
        Account account = new Account();
        try {
            account = bankService.getAccount(accountNumber);
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }

        return account;
    }

    public void deleteAccount(String cpr) throws PaymentException {
        try {
            Account account = bankService.getAccountByCprNumber(cpr);
            bankService.retireAccount(account.getId());
        } catch (BankServiceException_Exception e) {
            throw new PaymentException(e.getMessage());
        }
    }

    /*
     *  Bank Interactions
     */

    public void demo(JsonObject jsonObject){
        // Implement me
    }

    public PaymentStatus acceptPayment(UUID PaymentID, Token token) {
        /* TODO Communicate with TokenService to validate*/
        return null;
    }
}
