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

    @Given("a merchant with name {string} and accountNumber {string} that does not exist in the repository")
    public void aMerchantWithNameAndAccountNumberThatDoesNotExistInTheRepository(String name, String accountNumber) throws Throwable {
        merchant = new Merchant(name, accountNumber, null);
    }

    @Given("a merchant with name {string} and accountNumber {string} that does exist in the repository")
    public void aMerchantWithNameAndAccountNumberThatDoesExistInTheRepository(String name, String accountNumber) {
        merchant = new Merchant(name, accountNumber, null);
        try {
            service.registerMerchant(merchant);
            assertEquals(service.getMerchant(merchant.getMerchantID()), merchant);
        } catch (MerchantException e) {
            fail();
        }
    }

    @Then("a merchant with name {string} and accountNumber {string} exists in the repository")
    public void aMerchantWithNameAndAccountNumberExistsInTheRepository(String name, String accountNumber) throws Throwable {
        Merchant merchant1 = service.getMerchant(merchant.getMerchantID());
        assertEquals(merchant1, merchant);
        assertEquals(merchant1.getName(),name);
        assertEquals(merchant1.getAccountNumber(), accountNumber);
    }

    @Then("the merchant does not exist in the repository")
    public void aMerchantDoesNotExistInTheRepository() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        try {
            service.getMerchant(merchant.getMerchantID());
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

    @When("the new merchant is added to the repository")
    public void theNewMerchantIsAddedToTheRepository() {
        try {
            service.registerMerchant(merchant);
        } catch (MerchantException e) {
            exception = e;
        }
    }

    @When("the merchant is removed from the repository")
    public void theMerchantIsRemovedFromTheRepository() {
        try {
            service.removeMerchant(merchant.getMerchantID());
        } catch (MerchantException e) {
            exception = e;
        }
    }
}
