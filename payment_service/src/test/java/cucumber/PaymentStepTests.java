package cucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.Merchant;
import models.Payment;

public class PaymentStepTests {
    Merchant merchant = new Merchant();
    Payment payment;

    @Given("a merchant with ID {int} who wants a payment for {int} kroners")
    public void aMerchantWithIDWhoWantsAPaymentForKroners(int merchantID, int arg1) {
        merchant.setMerchantID(merchantID);
        payment = new Payment(merchantID, arg1);
    }

    @When("^he request the payment in the app$")
    public void heRequestThePaymentInTheApp() {
    }

    @Then("^he receives a paymentID with the type UUID$")
    public void heReceivesAPaymentIDWithTheTypeUUID() {
    }

    @And("the payment can be found using paymentID")
    public void thePaymentCanBeFoundUsingPaymentID() {
    }
}
