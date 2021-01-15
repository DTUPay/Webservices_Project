package dtupay;

import exceptions.MerchantException;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import models.Merchant;
import org.junit.jupiter.api.Assertions;

import static io.smallrye.common.constraint.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MerchantServiceSteps {
    private MerchantService service;
    private Merchant merchant;
    private MerchantException exception;

    @Before
    public void initialize_repository(){
        service = new MerchantService();
    }

    @Given("^a merchant with name \"([^\"]*)\" and CVR \"([^\"]*)\" that does not exist in the repository$")
    public void aMerchantWithNameAndCVRThatDoesNotExistInTheRepository(String arg0, String arg1) throws Throwable {
        merchant = new Merchant(arg0, arg1);
        try {
            service.getMerchant(arg1);
            fail();
        }
        catch (MerchantException e) {
            assertTrue(true);
        }
    }

    @Given("^a new merchant with name \"([^\"]*)\" and CVR \"([^\"]*)\"$")
    public void aNewMerchantWithNameAndCVR(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        merchant = new Merchant(arg0, arg1);
    }

    @Given("a merchant with name {string} and CVR {string} that does exist in the repository")
    public void aMerchantWithNameAndCVRThatDoesExistInTheRepository(String arg0, String arg1) {
        merchant = new Merchant(arg0, arg1);
        try {
            service.registerMerchant(merchant);
            assertEquals(service.getMerchant(arg1), merchant);
        } catch (MerchantException e) {
            fail();
        }
    }

    @Then("^a merchant with name \"([^\"]*)\" and CVR \"([^\"]*)\" exists in the repository$")
    public void aMerchantWithNameAndCVRExistsInTheRepository(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        Merchant merchant1 = service.getMerchant(arg1);
        assertEquals(service.getMerchant(arg1), merchant);
        assertEquals(service.getMerchant(arg1).getName(),arg0);
    }

    @Then("^a merchant with CVR \"([^\"]*)\" does not exist in the repository$")
    public void aMerchantWithCVRDoesNotExistInTheRepository(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        try {
            service.getMerchant(arg0);
            fail();
        }
        catch (MerchantException e) {
            assertTrue(true);
        }
    }

    @Then("^an error message with \"([^\"]*)\" is thrown$")
    public void anErrorMessageWithIsThrown(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        Assertions.assertEquals(arg0, this.exception.getMessage());
    }

    @When("^a merchant with CVR \"([^\"]*)\" is removed from the repository$")
    public void aMerchantWithCVRIsRemovedFromTheRepository(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        try {
            service.removeMerchant(arg0);
        } catch (MerchantException e) {
            exception = e;
        }
    }

    @When("^the new merchant is added to the repository$")
    public void theNewMerchantIsAddedToTheRepository() {
        try {
            service.registerMerchant(merchant);
        } catch (MerchantException e) {
            exception = e;
        }
    }
}
