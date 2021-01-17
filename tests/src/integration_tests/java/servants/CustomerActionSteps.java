package servants;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import static org.junit.Assert.fail;

/**
 * @author Björn Wilting s184214
 */
public class CustomerActionSteps {

    CustomerServant customer;
    Exception exception;


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
        try {
            customer.requestTokens(customer.getID(), arg0);
            Assert.assertEquals(customer.getCustomerTokens().size(),arg0);
        } catch (Exception e) {
            fail();
        }
    }


    @Then("the customer has been given {int} tokens")
    public void theCustomerHasBeenGivenTokens(int arg0) {
        Assert.assertEquals(customer.getCustomerTokens().size(), arg0);
    }

    @When("the customer requests {int} new tokens")
    public void theCustomerRequestsNewTokens(int arg0) {
        try {
            customer.requestTokens(customer.getID(), arg0);
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("the customer accepts the payment with id {string}")
    public void theCustomerAcceptsThePaymentWithId(String arg0) {
    }
}
