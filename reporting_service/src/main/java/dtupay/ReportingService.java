/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen
 */

package dtupay;


import brokers.ReportingBroker;
import dto.ReportRequestDTO;
import exceptions.TokenException;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import models.Payment;
import models.PaymentStatus;
import models.Report;
import models.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Oliver O. Nielsen
 */
@QuarkusMain
public class ReportingService {
    private static ReportingService instance = new ReportingService();
    ReportingBroker broker;
    PaymentRepository paymentRepository;

    public ReportingService() {
        this.paymentRepository = PaymentRepository.getInstance();
        broker = new ReportingBroker(this);
    }

    public static void main(String[] args) {
        ReportingService service = ReportingService.getInstance();
        Quarkus.run();
    }

    public static ReportingService getInstance(){
        return instance;
    }


    public void transactionUpdate(Payment payment){
        Payment existingPayment = paymentRepository.getPayment(payment.getPaymentID());
        // If new payment
        if(existingPayment == null){
            paymentRepository.addPayment(payment);
        }else{
            existingPayment = payment;
        }
    }

    public Report getManagerReport(){
        return new Report(paymentRepository.getPayments(), false);
    }

    public Report getMerchantReport(ReportRequestDTO reportRequestDTO){
        List<Payment> payments = paymentRepository
                .getPayments()
                .stream()
                .filter(p -> p.getDate().after(reportRequestDTO.getFromDate())
                        && p.getDate().before(reportRequestDTO.getToDate())
                        && p.getMerchantID().equals(reportRequestDTO.getMerchantID())
                )
                .collect(Collectors.toList());
        return new Report(payments, true);
    }

    public Report getCustomerReport(ReportRequestDTO reportRequestDTO){
        List<Payment> payments = paymentRepository
                .getPayments()
                .stream()
                .filter(p -> p.getDate().after(reportRequestDTO.getFromDate())
                        && p.getDate().before(reportRequestDTO.getToDate())
                        && p.getCustomerID().equals(reportRequestDTO.getCustomerID())
                )
                .collect(Collectors.toList());
        return new Report(payments, false);
    }


}
