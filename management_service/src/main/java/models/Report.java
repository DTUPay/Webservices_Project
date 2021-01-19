package models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Oliver O. Nielsen & Mikkel Rosenfeldt Anderson & Laura
 */
public class Report extends Payload{
    private List<Payment> payments;
    private double totalAmount;
    private int totalPayments;
    private int totalRefunded;
    private double result;

    /**
     * @param payments
     * @param anonymous Should hide CustomerID
     */
    public Report(List<Payment> payments, boolean anonymous){
        List<Payment> newPayments = new ArrayList<>();
        for (Payment payment:payments) {
            Payment newPayment = new Payment(payment);
            newPayment.setMerchantAccountID("redacted");
            newPayment.setCustomerAccountID("redacted");
            if(anonymous){
                newPayment.setCustomerID(null);
            }
            newPayments.add(newPayment);
        }
        this.payments = newPayments;
        this.totalAmount = getPayments().stream().mapToDouble(Payment::getAmount).sum();
        this.totalPayments = getPayments().size();
        List<Payment> refunds = getPayments().stream()
                .filter(p -> p.getStatus().equals(PaymentStatus.REFUNDED)).collect(Collectors.toList());
        this.totalRefunded = refunds.size();
        double totalRefundedAmount = refunds.stream().mapToDouble(Payment::getAmount).sum();
        this.result = this.totalAmount - totalRefundedAmount;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalPayments() {
        return totalPayments;
    }

    public void setTotalPayments(int totalPayments) {
        this.totalPayments = totalPayments;
    }

    public int getTotalRefunded() {
        return totalRefunded;
    }

    public void setTotalRefunded(int totalRefunded) {
        this.totalRefunded = totalRefunded;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
