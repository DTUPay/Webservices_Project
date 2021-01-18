package dtupay;

import brokers.PaymentBroker;
import dto.*;
import dtu.ws.fastmoney.*;
import exceptions.BankException;
import exceptions.PaymentException;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import models.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Mikkel Rosenfeldt Anderson & Laura & Benjamin
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

    public UUID createPayment(PaymentDTO paymentDTO, CustomerDTO customerDTO, TokenDTO tokenDTO) throws BankServiceException_Exception {
        Payment payment = new Payment(paymentDTO.getMerchantID(), paymentDTO.getAmount());
        payment.setCustomerID(customerDTO.getCustomerID());
        payment.setTokenID(tokenDTO.getTokenID());
        payment.setCustomerAccountID(customerDTO.getAccountNumber());
        payment.setMerchantAccountID(paymentDTO.getMerchantAccountID());

        System.out.println("Create payment account numbers: " + payment.getCustomerAccountID() + " and " + payment.getMerchantAccountID());
        bankService.transferMoneyFromTo(
                customerDTO.getAccountNumber(), // Money from
                paymentDTO.getMerchantAccountID(), // Money to
                BigDecimal.valueOf(payment.getAmount()),
                "New payment: " + payment.getPaymentID());

        payment.setStatus(PaymentStatus.COMPLETED);
        paymentRepository.addPayment(payment);
        return payment.getPaymentID();
    }

    public void refundPayment(RefundDTO refundDTO, TokenDTO tokenDTO) throws BankServiceException_Exception, BankException {
        Payment payment = paymentRepository.getPayment(refundDTO.getPaymentID());
        if(payment.getStatus() != PaymentStatus.COMPLETED){
            throw new BankException("Payment already refunded");
        }
        System.out.println("MerchantAccount:" + payment.getMerchantAccountID());
        System.out.println("CustomerAccount:" + payment.getCustomerAccountID());
        System.out.println("Amount: " + payment.getAmount());
        System.out.println("Refunding payment...");
        bankService.transferMoneyFromTo(
                payment.getMerchantAccountID(), // Money from
                payment.getCustomerAccountID(), // Money to
                BigDecimal.valueOf(payment.getAmount()),
                "Refund payment: " + payment.getPaymentID());
        System.out.println("Payment refunded");
        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setRefundTokenID(tokenDTO.getTokenID());
    }
}
