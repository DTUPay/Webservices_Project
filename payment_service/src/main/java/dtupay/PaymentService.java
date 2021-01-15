package dtupay;

import dtu.ws.fastmoney.*;
import exceptions.BankException;
import exceptions.PaymentException;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import models.Customer;
import models.Payment;
import models.PaymentStatus;
import models.Token;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Mikkel & Laura & Benjamin
 */
@QuarkusMain
public class PaymentService {
    public HashMap<UUID, AsyncResponse> pendingRequests = new HashMap<>();
    IPaymentRepository paymentRepository;
    BankService bankService = new BankServiceService().getBankServicePort();
    RabbitMq rabbitMq;

    // <editor-fold desc="Repository Interactions">
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

    public List<Payment> getCustomerSummary(String cpr) {
        return paymentRepository.getPayments(cpr);
    }


    //</editor-fold>

    // <editor-fold desc="Bank Interactions">
    /*
     *  Bank Interactions
     */
    public String createAccountWithBalance(Customer customer, int amount) throws BankException {
        User user = customer.customerToUser();
        String accountNumber;
        try {
            accountNumber = bankService.createAccountWithBalance(user, BigDecimal.valueOf(amount));
        } catch (BankServiceException_Exception e) {
            throw new BankException(e.getMessage());
        }
        return accountNumber;
    }

    public Account getAccount(String accountNumber) throws BankException {
        Account account = new Account();
        try {
            account = bankService.getAccount(accountNumber);
        } catch (BankServiceException_Exception e) {
            throw new BankException(e.getMessage());
        }
        return account;
    }

    /**
     * Gets all accounts
     * @return List with accounts
     * @throws BankException
     */
    public List<Account> getAccounts() throws BankException {
        List<Account> accounts = new ArrayList<>();
        List<AccountInfo> accountInfos = bankService.getAccounts();
        for (AccountInfo accountInfo : accountInfos) {
            try {
                Account account = bankService.getAccount(accountInfo.getAccountId());
                accounts.add(account);
            } catch (BankServiceException_Exception e) {
                throw new BankException(e.getMessage());
            }
        }
        return accounts;
    }

    public void deleteAccount(String cpr) throws PaymentException {
        try {
            Account account = bankService.getAccountByCprNumber(cpr);
            bankService.retireAccount(account.getId());
        } catch (BankServiceException_Exception e) {
            throw new PaymentException(e.getMessage());
        }
    }

    public PaymentStatus acceptPayment(UUID paymentID, UUID tokenID, String cpr) {
        System.out.println("Start");
        System.out.println(paymentID);
        System.out.println(tokenID);
        System.out.println(cpr);
        System.out.println("End");
        /* TODO Communicate with TokenService to validate*/
        /* Token_service.isTokenValid(tokenId,cpr)*/
        /* remove token */
        /* Set payment to valid */

        return PaymentStatus.PENDING;
    }

    private String validateToken(UUID tokenID) {
        // TODO validate in token service
        return "";
    }

    // </editor-fold>

    //<editor-fold desc="Rabbit MQ">
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

    public void acceptPayment(JsonObject jsonObject){
        JsonObject payload = (JsonObject) jsonObject.get("payload");
        JsonObject callback = (JsonObject) jsonObject.get("callback");
        String callbackService = callback.get("service").toString().replaceAll("\"", "");
        String callbackEvent = callback.get("event").toString().replaceAll("\"", "");

        //Call actual function with data from
        PaymentStatus paymentStatus = acceptPayment(
                UUID.fromString(payload.get("paymentID").toString().replaceAll("\"", "")),
                UUID.fromString(payload.get("tokenID").toString().replaceAll("\"", "")),
                payload.get("cpr").toString().replaceAll("\"", "")
        );

        //Create payload JSON
        JsonObject responsePayload = Json.createObjectBuilder()
                .add("paymentStatus", new Gson().toJson(paymentStatus)).build();

        String uuid = jsonObject.get("requestId").toString();
        JsonObject response = Json.createObjectBuilder()
                .add("requestId", uuid)
                .add("messageId", UUID.randomUUID().toString())
                .add("event", callbackEvent)
                .add("payload", responsePayload)
                .build();

        System.out.println("Response created");
        rabbitMq.sendMessage(callbackService, response, null);
    }
    //</editor-fold>
}
