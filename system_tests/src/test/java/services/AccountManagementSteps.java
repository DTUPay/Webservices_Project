package services;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AccountManagementSteps {
    @Given("a user with the name {string} {string} and the cpr {string}")
    public void aUserWithTheNameAndTheCpr(String arg0, String arg1, String arg2) {
    }

    @When("the user registers for the app")
    public void theUserRegistersForTheApp() {
    }

    @Then("a customer with the name {string} {string} and the cpr {string} has been created")
    public void aCustomerWithTheNameAndTheCprHasBeenCreated(String arg0, String arg1, String arg2) {
    }

    @Given("a company with the name {string} and cvr {string}")
    public void aCompanyWithTheNameAndCvr(String arg0, String arg1) {
    }

    @When("the company registers for the app")
    public void theCompanyRegistersForTheApp() {
    }

    @Then("a merchant with the name {string} and cvr {string} has been created")
    public void aMerchantWithTheNameAndCvrHasBeenCreated(String arg0, String arg1) {
    }
}
