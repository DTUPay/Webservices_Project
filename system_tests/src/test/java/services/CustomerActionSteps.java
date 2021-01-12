package services;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CustomerActionSteps {
    @Given("a customer with a token")
    public void aCustomerWithAToken() {
    }

    @Then("the token is consumed")
    public void theTokenIsConsumed() {
    }

    @And("the payment succeeds")
    public void thePaymentSucceeds() {
    }

    @Given("a customer without tokens")
    public void aCustomerWithoutTokens() {
    }

    @Then("the payment fails")
    public void thePaymentFails() {
    }

    @Given("a customer with {int} tokens")
    public void aCustomerWithTokens(int arg0) {
    }

    @When("the customer requests new tokens")
    public void theCustomerRequestsNewTokens() {
    }

    @Then("the customer is given {int} tokens")
    public void theCustomerIsGivenTokens(int arg0) {
    }

    @Then("the token request is denied")
    public void theTokenRequestIsDenied() {
    }

    @And("the customer has {int} tokens")
    public void theCustomerHasTokens(int arg0) {
    }

    @And("a pending payment with the payment id {string} exists")
    public void aPendingPaymentWithThePaymentIdExists(String arg0) {
    }

    @When("the customer accepts the payment with id {string}")
    public void theCustomerAcceptsThePaymentWithId(String arg0) {
    }

    @And("a pending payment with the payment id {string} does not exists")
    public void aPendingPaymentWithThePaymentIdDoesNotExists(String arg0) {
    }
}
