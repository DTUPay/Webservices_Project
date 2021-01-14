package servants;

import io.cucumber.java.en.And;
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

    @Given("a customer with id {string}")
    public void aCustomerWithId(String arg0) {
    }

    @When("the customer requests to be deleted")
    public void theCustomerRequestsToBeDeleted() {
    }

    @Then("the customer with id {string} is deleted from the system")
    public void theCustomerWithIdIsDeletedFromTheSystem(String arg0) {
    }

    @And("there is no customer with id {string} registered in the system")
    public void thereIsNoCustomerWithIdRegisteredInTheSystem(String arg0) {
    }

    @Given("a merchant with id {string}")
    public void aMerchantWithId(String arg0) {
    }

    @When("the merchant requests to be deleted")
    public void theMerchantRequestsToBeDeleted() {
    }

    @Then("the merchant with id {string} is deleted from the system")
    public void theMerchantWithIdIsDeletedFromTheSystem(String arg0) {
    }

    @And("there is no merchant with id {string} registered in the system")
    public void thereIsNoMerchantWithIdRegisteredInTheSystem(String arg0) {
    }
}
