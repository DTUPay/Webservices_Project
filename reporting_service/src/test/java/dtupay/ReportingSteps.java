package dtupay;

import dto.ReportRequestDTO;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.PaymentStatus;
import models.Report;
import models.Token;
import testModels.TestPayment;

import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author Oliver O. Nielsen & Rubatharisan Thirumathyam & Mikkel Rosenfeldt Anderson & Laura
 */
public class ReportingSteps {
    ReportingService reportingService = ReportingService.getInstance();;
    UUID merchantID = UUID.randomUUID();
    UUID customerID = UUID.randomUUID();
    Report managerReport = null;
    Report merchantReport = null;
    Report customerReport = null;

    @After
    public void cleanup(){
        reportingService.paymentRepository.removeAllPayments();
    }

    @Given("{int} transactions have been made")
    public void transactionsHaveBeenMade(int arg0) {
        //Generate merchant & customer transactions
        for(int i = 0; i < arg0; i++){
            //Merchant transaction
            TestPayment payment = new TestPayment(merchantID, new Random().nextInt(10000)+1);
            Token token = new Token(UUID.randomUUID());
            payment.setTokenID(token.getTokenID());
            payment.setCustomerID(customerID);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -i);
            payment.setDate(calendar.getTime());
            if(new Random().nextInt(10) == 9)
                payment.setStatus(PaymentStatus.REFUNDED);
            reportingService.paymentRepository.addPayment(payment);

            //Customer transaction
            payment = new TestPayment(UUID.randomUUID(), new Random().nextInt(10000)+1);
            token = new Token(customerID);
            payment.setTokenID(token.getTokenID());
            payment.setCustomerID(customerID);
            payment.setDate(calendar.getTime());
            if(new Random().nextInt(10) == 9)
                payment.setStatus(PaymentStatus.REFUNDED);
            reportingService.paymentRepository.addPayment(payment);
        }
    }

    @When("a {string} requests a report")
    public void aRequestsAReport(String arg0) {
        ReportRequestDTO dto = new ReportRequestDTO();
        Calendar calendar = Calendar.getInstance();
        dto.setToDate(calendar.getTime());
        calendar.add(Calendar.DATE, -10);
        dto.setFromDate(calendar.getTime());

        switch (arg0) {
            case "manager":
                managerReport = reportingService.getManagerReport();
                break;
            case "customer":
                dto.setCustomerID(customerID);
                customerReport = reportingService.getCustomerReport(dto);
                break;
            case "merchant":
                dto.setMerchantID(merchantID);
                merchantReport = reportingService.getMerchantReport(dto);
                break;
        }
    }

    @Then("a customer report is received with {int} transactions")
    public void aCustomerReportIsReceivedWithTransactions(int arg0) {
        assertNotNull(customerReport);
        assertEquals(arg0, customerReport.getPayments().size());
        assertTrue(customerReport.getPayments().stream().allMatch(payment -> payment.getCustomerID() == this.customerID));
    }

    @Then("a merchant report is received with {int} transactions")
    public void aMerchantReportIsReceivedWithTransactions(int arg0) {
        assertNotNull(merchantReport);
        assertEquals(arg0, merchantReport.getPayments().size());
        assertTrue(merchantReport.getPayments().stream().allMatch(payment -> payment.getCustomerID() == null));
        assertTrue(merchantReport.getPayments().stream().allMatch(payment -> payment.getMerchantID() == this.merchantID));
    }

    @Then("a manager report is received with {int} transactions")
    public void aManagerReportIsReceivedWithTransactions(int arg0) {
        assertNotNull(managerReport);
        assertEquals(arg0, managerReport.getPayments().size());
    }
}
