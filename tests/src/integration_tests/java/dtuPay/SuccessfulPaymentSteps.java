package dtuPay;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert.*;
import servants.CustomerServant;
import servants.MerchantServant;

import static org.junit.Assert.*;

public class SuccessfulPaymentSteps {
    private CustomerServant customer;
    private MerchantServant merchant;
    private Exception exception;
    private boolean success;
    private int tokenCount;

    @Given("the customer has 5 tokens")
    public void theCustomerHasTokens(int arg0) {
        assertEquals(customer.getCustomerTokens().size(), 0);
        try {
            customer.requestTokens(5);
            tokenCount = customer.getCustomerTokens().size();
            assertEquals(tokenCount, 5);
        } catch (Exception e) {
            fail();
        }
    }

    @Given("a customer with name {string} {string} and cpr number {string} and balance {int} DKK has a bank account")
    public void aCustomerWithNameAndCprNumberAndBalanceDKKHasABankAccount(String arg0, String arg1, String arg2, int arg3) {
        customer = new CustomerServant(arg0 + " "+ arg1, arg2, arg3);
        //TODO: Register customer with bank
        /* Using management servant?*/
    }

    @Given("the customer is registered with DTU Pay")
    public void theCustomerIsRegisteredWithDTUPay() {
        //TODO: Register the customer with DTU pay using management servant
        //TODO: customer.setID(customerID);
    }

    @Given("a merchant with name {string} {string} and cpr number {string} and balance {int} DKK has a bank account")
    public void aMerchantWithNameAndCprNumberAndBalanceDKKHasABankAccount(String arg0, String arg1, String arg2, int arg3) {
        merchant = new MerchantServant(arg0 + " " + arg1, arg2, arg3);
        //TODO: Register merchant with bank
        /* Using management servant? */
    }

    @Given("the merchant is registered with DTU Pay")
    public void theMerchantIsRegisteredWithDTUPay() {
        //TODO: Register the merchant with DTU Pay using management servant
        //TODO: merchant.setID(merchantID);
    }

    @When("the customer authorizes a payment of {int} to the merchant")
    public void theCustomerAuthorizesAPaymentOfToTheMerchant(int arg0) {
        try {
            customer.acceptPayment(arg0, merchant.getID(), customer.selectToken());
            success = true;
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("the customer request to see his account balance")
    public void theCustomerRequestToSeeHisAccountBalance() {
        //TODO: Request the customers account balance
        //TODO: customer.setBalance(output)
    }

    @When("the merchant request to see his account balance")
    public void theMerchantRequestToSeeHisAccountBalance() {
        //TODO: Request the merchants account balance
        //TODO: merchant.setBalance(output)
    }

    @Then("the payment succeeds")
    public void thePaymentSucceeds() {
        assertTrue(success);
    }

    @Then("the customer has {int} DKK in his account")
    public void theCustomerHasDKKInHisAccount(int arg0) {
        assertEquals(customer.getBalance(), arg0);
    }

    @Then("the merchant has {int} DKK in his account")
    public void theMerchantHasDKKInHisAccount(int arg0) {
        assertEquals(merchant.getBalance(), arg0);
    }

    @Then("the token is consumed")
    public void theTokenIsConsumed() {
        assertEquals(tokenCount-1,customer.getCustomerTokens().size());
    }
}
