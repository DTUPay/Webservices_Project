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
    IPaymentRepository paymentRepository;
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

    public UUID requestPayment(int amount, UUID merchantID) throws PaymentException{
        if (merchantID == null) {
            throw new PaymentException("Non positive merchant ID");
        }
        if (amount <= 0) {
            throw new PaymentException("Non positive payment amount");
        }
        Payment newPayment = new Payment(merchantID, amount);
        paymentRepository.addPayment(newPayment);
        return newPayment.getPaymentID();
    }

    public List<Payment> getManagerSummary() {
        return paymentRepository.getPayments();
    }

    public PaymentStatus acceptPayment(UUID paymentID, UUID tokenID) {
        System.out.println("Start");
        System.out.println(paymentID);
        System.out.println(tokenID);
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


    public void getRefund(RefundDTO refund, String customerAccountID, String merchantAccountID) throws PaymentException, BankServiceException_Exception {
        Payment payment = paymentRepository.getPayment(refund.getPaymentID());
        if(payment == null)
            throw new PaymentException("Payment with id " + refund.getPaymentID() + " not found.");

        //Get customer account number from customer service
        //Get merchant account number from merchant service

        bankService.transferMoneyFromTo(
                payment.getMerchantID().toString(),
                payment.getCustomerID().toString(),
                BigDecimal.valueOf(payment.getAmount()),
                "Refund of payment: " + payment.getPaymentID());

        payment.setStatus(PaymentStatus.REFUNDED);
    }

    public UUID createPayment(PaymentDTO paymentDTO, CustomerDTO customerDTO, TokenDTO tokenDTO) throws BankServiceException_Exception {
        Payment payment = new Payment(paymentDTO.getMerchantID(), paymentDTO.getAmount());
        payment.setCustomerID(customerDTO.getCustomerID());
        payment.setTokenID(tokenDTO.getTokenID());
        payment.setCustomerAccountID(customerDTO.getAccountNumber());
        payment.setMerchantAccountID(paymentDTO.getMerchantAccountID());

        bankService.transferMoneyFromTo(
                customerDTO.getAccountNumber(), // Money from
                paymentDTO.getMerchantAccountID(), // Money to
                BigDecimal.valueOf(payment.getAmount()),
                "New payment: " + payment.getPaymentID());

        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.addPayment(payment);
        return payment.getPaymentID();
    }

    public void refundPayment(RefundDTO refundDTO, TokenDTO tokenDTO) throws BankServiceException_Exception {
        Payment payment = paymentRepository.getPayment(refundDTO.getPaymentID());
        bankService.transferMoneyFromTo(
                payment.getMerchantAccountID(), // Money from
                payment.getCustomerAccountID(), // Money to
                BigDecimal.valueOf(payment.getAmount()),
                "Refund payment: " + payment.getPaymentID());

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setRefundTokenID(tokenDTO.getTokenID());
    }
}
