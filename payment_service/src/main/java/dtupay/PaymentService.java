package dtupay;

import brokers.PaymentBroker;
import dto.CustomerDTO;
import dto.PaymentDTO;
import dto.RefundDTO;
import dto.TokenDTO;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import exceptions.BankException;
import exceptions.PaymentException;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import models.Payment;
import models.PaymentStatus;

import javax.ws.rs.container.AsyncResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Olier O. Nielsen & Mikkel Rosenfeldt Anderson & Laura & Benjamin
 */
@QuarkusMain
public class PaymentService {
    private static PaymentService instance = new PaymentService();
    public HashMap<UUID, AsyncResponse> pendingRequests = new HashMap<>();
    IPaymentRepository paymentRepository = PaymentRepository.getInstance();
    BankService bankService = new BankServiceService().getBankServicePort();
    PaymentBroker broker;
    Gson gson = new Gson();

    public PaymentService() {
        broker = new PaymentBroker(this);
    }

    public static void main(String[] args) {
        PaymentService service = new PaymentService().getInstance();
        Quarkus.run();
    }

    public static PaymentService getInstance(){
        return instance;
    }

    public Payment getPayment(UUID PaymentID) throws PaymentException {
        Payment payment = paymentRepository.getPayment(PaymentID);
        if (payment == null) {
            throw new PaymentException("Payment with ID: " + PaymentID + " not found.");
        }
        return payment;
    }


    public UUID createPayment(PaymentDTO paymentDTO, CustomerDTO customerDTO, TokenDTO tokenDTO) throws BankException {
        Payment payment = new Payment(paymentDTO.getMerchantID(), paymentDTO.getAmount());
        payment.setCustomerID(customerDTO.getCustomerID());
        payment.setTokenID(tokenDTO.getTokenID());
        payment.setCustomerAccountID(customerDTO.getAccountNumber());
        payment.setMerchantAccountID(paymentDTO.getMerchantAccountID());

        System.out.println("Create payment account numbers: " + payment.getCustomerAccountID() + " and " + payment.getMerchantAccountID());
        try {
            bankService.transferMoneyFromTo(
                    customerDTO.getAccountNumber(), // Money from
                    paymentDTO.getMerchantAccountID(), // Money to
                    BigDecimal.valueOf(payment.getAmount()),
                    "New payment: " + payment.getPaymentID());

            payment.setStatus(PaymentStatus.COMPLETED);
            paymentRepository.addPayment(payment);
            return payment.getPaymentID();
        }
        catch (BankServiceException_Exception e ) {
            throw new BankException(e.getMessage());
        }
    }

    public void refundPayment(RefundDTO refundDTO, TokenDTO tokenDTO) throws BankException, PaymentException {
        Payment payment = paymentRepository.getPayment(refundDTO.getPaymentID());
        if(payment.getStatus() != PaymentStatus.COMPLETED){
            throw new PaymentException("Payment already refunded");
        }
        System.out.println("MerchantAccount:" + payment.getMerchantAccountID());
        System.out.println("CustomerAccount:" + payment.getCustomerAccountID());
        System.out.println("Amount: " + payment.getAmount());
        System.out.println("Refunding payment...");
        try {
            bankService.transferMoneyFromTo(
                    payment.getMerchantAccountID(), // Money from
                    payment.getCustomerAccountID(), // Money to
                    BigDecimal.valueOf(payment.getAmount()),
                    "Refund payment: " + payment.getPaymentID());
        } catch (BankServiceException_Exception e) {
            throw new BankException(e.getMessage());
        }
        System.out.println("Payment refunded");
        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setRefundTokenID(tokenDTO.getTokenID());
    }
}
