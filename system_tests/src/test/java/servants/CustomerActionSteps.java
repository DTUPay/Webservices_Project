package servants;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
/**
 * @author Björn Wilting s184214
 */
public class CustomerActionSteps {

    CustomerServant customer;


    @And("the payment succeeds")
    public void thePaymentSucceeds() {
    }

    @And("the customer has {int} tokens")
    public void theCustomerHasTokens(int arg0) {
    }

    @And("a pending payment with the payment id {string} exists")
    public void aPendingPaymentWithThePaymentIdExists(String arg0) {
    }

    @And("a pending payment with the payment id {string} does not exists")
    public void aPendingPaymentWithThePaymentIdDoesNotExists(String arg0) {
    }

    @Given("a customer with {int} token\\(s)")
    public void aCustomerWithTokens(int arg0) {
        customer = new CustomerServant("TestID");
        customer.requestTokens(arg0);
    }

    @Then("the token is consumed")
    public void theTokenIsConsumed() {
    }


    @Then("the payment fails")
    public void thePaymentFails() {
    }

    @Then("the customer is given {int} tokens")
    public void theCustomerIsGivenTokens(int arg0) {
    }

    @Then("the token request is denied")
    public void theTokenRequestIsDenied() {
    }

    @When("the customer requests new tokens")
    public void theCustomerRequestsNewTokens() {
    }

    @When("the customer accepts the payment with id {string}")
    public void theCustomerAcceptsThePaymentWithId(String arg0) {
    }
}
