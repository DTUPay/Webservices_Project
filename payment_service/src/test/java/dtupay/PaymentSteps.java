package dtupay;

import exceptions.PaymentException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.Payment;
import org.junit.Before;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author Mikkel & Laura
 */
public class PaymentSteps {
    PaymentRepository repo = new PaymentRepository();
    Payment payment;
    int merchantID;
    int amount;
    UUID paymentID;
    Random random = new Random(System.currentTimeMillis());
    String errorMessage = "";
    List<Payment> summary = new ArrayList<>();

    @Before()
    public void init(){
        merchantID = 0;
        amount = 0;
        paymentID = UUID.randomUUID();
    }

    @Given("a merchant with ID {int} who wants a payment for {int} kroners")
    public void aMerchantWithIDWhoWantsAPaymentForKroners(int merchantID, int amount) {
        this.merchantID = merchantID;
        this.amount = amount;
    }

    @When("he request the payment in the app")
    public void heRequestThePaymentInTheApp() {
        try {
            paymentID = repo.requestPayment(amount, merchantID);
        } catch (PaymentException e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("he receives a paymentID with the type UUID")
    public void heReceivesAPaymentIDWithTheTypeUUID() {
        assertEquals(paymentID.getClass(), UUID.class);
    }

    @And("the payment can be found using paymentID")
    public void thePaymentCanBeFoundUsingPaymentID() {
        try {
            payment = repo.getPayment(paymentID);
        } catch (PaymentException e) {
            errorMessage = e.getMessage();
        }
        assertNotNull(payment);
    }

    @When("the manager requests a summary")
    public void theManagerRequestsASummary() {
        summary = repo.getManagerSummary();
    }

    @Given("there are {int} payments made")
    public void thereArePaymentsMade(int arg0) {
        int randomAmount = random.nextInt(Integer.SIZE - 1); // ensure positive number
        int randomMerchantId = random.nextInt(Integer.SIZE - 1); // ensure positive number
        for (int i = 0; i < arg0; i++) {
            try {
                repo.requestPayment(randomAmount,randomMerchantId);
            } catch (PaymentException e) {
                e.printStackTrace();
            }
        }
    }

    @Then("he gets a summary with {int} payments")
    public void heGetsASummaryWithPayments(int arg0) {
        assertEquals(arg0,summary.size());
    }

    @And("an exception is made with the message {string}")
    public void anExceptionIsMadeWithTheMessage(String arg0) {
        assertEquals(arg0,errorMessage);
    }
}
