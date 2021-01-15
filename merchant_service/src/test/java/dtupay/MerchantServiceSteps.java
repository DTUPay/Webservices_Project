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

    @Given("a merchant with name {string} and CVR {string} that does not exist in the repository")
    public void aMerchantWithNameAndCVRThatDoesNotExistInTheRepository(String name, String cvr) throws Throwable {
        merchant = new Merchant(name, cvr);
        try {
            service.getMerchant(cvr);
            fail();
        }
        catch (MerchantException e) {
            assertTrue(true);
        }
    }

    @Given("a new merchant with name {string} and CVR {string}")
    public void aNewMerchantWithNameAndCVR(String name, String cvr) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        merchant = new Merchant(name, cvr);
    }

    @Given("a merchant with name {string} and CVR {string} that does exist in the repository")
    public void aMerchantWithNameAndCVRThatDoesExistInTheRepository(String name, String cvr) {
        merchant = new Merchant(name, cvr);
        try {
            service.registerMerchant(merchant);
            assertEquals(service.getMerchant(cvr), merchant);
        } catch (MerchantException e) {
            fail();
        }
    }

    @Then("a merchant with name {string} and CVR {string} exists in the repository")
    public void aMerchantWithNameAndCVRExistsInTheRepository(String name, String cvr) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        Merchant merchant1 = service.getMerchant(cvr);
        assertEquals(service.getMerchant(cvr), merchant);
        assertEquals(service.getMerchant(cvr).getName(),name);
    }

    @Then("a merchant with CVR {string} does not exist in the repository")
    public void aMerchantWithCVRDoesNotExistInTheRepository(String cvr) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        try {
            service.getMerchant(cvr);
            fail();
        }
        catch (MerchantException e) {
            assertTrue(true);
        }
    }

    @Then("an error message with {string} is thrown")
    public void anErrorMessageWithIsThrown(String msg) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        Assertions.assertEquals(msg, this.exception.getMessage());
    }

    @When("a merchant with CVR {string} is removed from the repository")
    public void aMerchantWithCVRIsRemovedFromTheRepository(String cvr) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        try {
            service.removeMerchant(cvr);
        } catch (MerchantException e) {
            exception = e;
        }
    }

    @When("the new merchant is added to the repository")
    public void theNewMerchantIsAddedToTheRepository() {
        try {
            service.registerMerchant(merchant);
        } catch (MerchantException e) {
            exception = e;
        }
    }
}
