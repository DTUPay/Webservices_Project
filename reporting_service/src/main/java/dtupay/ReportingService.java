/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen
 */

package dtupay;


import brokers.ReportingBroker;
import dto.ReportRequestDTO;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import models.Payment;
import models.Report;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Oliver O. Nielsen & Mikkel Rosenfeldt Anderson & Laura
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

    public List<Payment> getPayments(UUID id, boolean isMerchant) {
        if (isMerchant) {
            return paymentRepository.getPayments().stream().filter(payment -> payment.getMerchantID().equals(id)).collect(Collectors.toList());
        } else {
            return paymentRepository.getPayments().stream().filter(payment -> payment.getCustomerID().equals(id)).collect(Collectors.toList());
        }
    }

    public Report getManagerReport(){
        return new Report(paymentRepository.getPayments(), false);
    }

    public Report getMerchantReport(ReportRequestDTO reportRequestDTO){
        List<Payment> payments = getPayments(reportRequestDTO.getMerchantID(),true)
                .stream()
                .filter(p -> p.getDate().after(reportRequestDTO.getFromDate())
                        && p.getDate().before(reportRequestDTO.getToDate())
                )
                .collect(Collectors.toList());
        return new Report(payments, true);
    }

    public Report getCustomerReport(ReportRequestDTO reportRequestDTO){
        List<Payment> payments = getPayments(reportRequestDTO.getCustomerID(),false)
                .stream()
                .filter(p -> p.getDate().after(reportRequestDTO.getFromDate())
                        && p.getDate().before(reportRequestDTO.getToDate())
                )
                .collect(Collectors.toList());
        return new Report(payments, false);
    }
}
