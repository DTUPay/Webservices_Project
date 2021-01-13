package dtupay;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AdminSteps {
    @Given("An admin user")
    public void anAdminUser() {
        throw new PendingException();

    }

    @And("a user of type {string} with information firstname {string}, lastname {string} and cpr {string}")
    public void aUserOfTypeWithInformationFirstnameLastnameAndCpr(String arg0, String arg1, String arg2, String arg3) {
        throw new PendingException();
    }

    @When("admin creates user")
    public void adminCreatesUser() {
        throw new PendingException();
    }

    @Then("the user of type {string} has been added to the system")
    public void theUserOfTypeHasBeenAddedToTheSystem(String arg0) {
        throw new PendingException();
    }

    @And("a user of type {string} with cpr {string}")
    public void aUserOfTypeWithCpr(String arg0, String arg1) {
        throw new PendingException();
    }

    @When("admin removes the user")
    public void adminRemovesTheUser() {
        throw new PendingException();
    }

    @Then("the user has been removed from the system")
    public void theUserHasBeenRemovedFromTheSystem() {
        throw new PendingException();
    }
}
