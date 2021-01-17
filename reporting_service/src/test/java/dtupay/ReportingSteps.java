package dtupay;

import dto.ReportRequestDTO;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.*;
import org.junit.Assert;
import org.junit.Test;
import testModels.TestPayment;


import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */
public class ReportingSteps {

    Merchant merchant;
    Token token;
    ReportingService reportingService = ReportingService.getInstance();
    UUID merchantID = UUID.randomUUID();
    UUID customerID = UUID.randomUUID();
    Report managerReport = null;
    Report merchantReport = null;
    Report customerReport = null;

    @Before
    public void generateTransactions(){
        System.out.println("before is running");
        //Generate merchant & customer transactions
        for(int i = 0; i < 100; i++){
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

    @After
    public void cleanup(){
        //reportingService.paymentRepository.removeAllPayments();
    }

    @Given("a manager requesting a report")
    public void a_manager_requesting_a_report() {
        managerReport = reportingService.getManagerReport();
    }

    @Then("a manager report is received")
    public void a_manager_report_is_received() {
        Assert.assertNotNull(managerReport);
        //Must include all payments
        //Assert.assertEquals(managerReport.getPayments().size(), reportingService.paymentRepository.getPayments());
        System.out.println("done");
    }

    @Given("a merchant requesting a report")
    public void a_merchant_requesting_a_report() {
        ReportRequestDTO dto = new ReportRequestDTO();
        dto.setMerchantID(merchantID);
        Calendar calendar = Calendar.getInstance();
        dto.setToDate(calendar.getTime());
        calendar.add(Calendar.DATE, -10);
        dto.setFromDate(calendar.getTime());

        merchantReport = reportingService.getMerchantReport(dto);
    }

    @Then("a merchant report is received")
    public void a_merchant_report_is_received() {
        Assert.assertNotNull(merchantReport);
        Assert.assertEquals(merchantReport.getPayments().size(), 10);
        Assert.assertNull(merchantReport.getPayments().get(0).getCustomerID());
    }

    @Given("a customer requesting a report")
    public void a_customer_requesting_a_report() {
        ReportRequestDTO dto = new ReportRequestDTO();
        dto.setCustomerID(customerID);
        Calendar calendar = Calendar.getInstance();
        dto.setToDate(calendar.getTime());
        calendar.add(Calendar.DATE, -10);
        dto.setFromDate(calendar.getTime());

        customerReport = reportingService.getCustomerReport(dto);
    }

    @Then("a customer report is received")
    public void a_customer_report_is_received() {
        Assert.assertNotNull(customerReport);
        //Assert.assertEquals(customerReport.getPayments().size(), 10);
    }

}
