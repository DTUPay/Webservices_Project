package dtuPay;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.After;
import org.junit.Before;
import servants.*;

import static org.junit.Assert.*;

public class SuccessfulPaymentSteps {
    private CustomerServant customerAccount;
    private MerchantServant merchantAccount;
    private ManagementServant accountManagement;
    private Customer customer;
    private Merchant merchant;
    private Exception exception;
    private boolean success;
    private int tokenCount;

    @Before
    public void CreateUsersInBank() {
        //TODO: Create customer and merchant bank accounts
        //Customer: Jens Jensen, 121012-xxxx, 100 DKK
        customer = new Customer("Jens Jensen", "121012-xxxx",100);
        //Merchant: Mads Madsen, 140467-xxxx, 1000 DKK
        merchant = new Merchant("Mads Madsen", "140467", 1000);
        //TODO: Store account numbers
        accountManagement = new ManagementServant();
    }

    @Given("the customer has 5 tokens")
    public void theCustomerHasTokens(int arg0) {
        assertEquals(customerAccount.getCustomerTokens().size(), 0);
        try {
            customerAccount.requestTokens(customerAccount.getID(),5);
            tokenCount = customerAccount.getCustomerTokens().size();
            assertEquals(tokenCount, 5);
        } catch (Exception e) {
            fail();
        }
    }

    @Given("a customer with name {string} {string} and cpr number {string} and balance {int} DKK has a bank account")
    public void aCustomerWithNameAndCprNumberAndBalanceDKKHasABankAccount(String arg0, String arg1, String arg2, int arg3) {
        //TODO: Assert that customer is registered with the bank
    }

    @Given("the customer is registered with DTU Pay")
    public void theCustomerIsRegisteredWithDTUPay() {
        //TODO: Register the customer with DTU pay using management servant
        //TODO: Set customer id from returned accountID
        customerAccount = new CustomerServant(accountManagement.registerCustomer(customer));
    }

    @Given("a merchant with name {string} {string} and cpr number {string} and balance {int} DKK has a bank account")
    public void aMerchantWithNameAndCprNumberAndBalanceDKKHasABankAccount(String arg0, String arg1, String arg2, int arg3) {
        merchantAccount = new MerchantServant(arg0 + " " + arg1, arg2, arg3);
        //TODO: Assert that the merchant is registered with the bank
    }

    @Given("the merchant is registered with DTU Pay")
    public void theMerchantIsRegisteredWithDTUPay() {
        //TODO: Register the merchant with DTU Pay using management servant
        //TODO: merchant.setID(merchantID);
    }

    @When("the customer authorizes a payment of {int} to the merchant")
    public void theCustomerAuthorizesAPaymentOfToTheMerchant(int arg0) {
        try {
            customerAccount.acceptPayment(arg0, merchantAccount.getID(), customerAccount.selectToken());
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
        assertEquals(merchantAccount.getBalance(), arg0);
    }

    @Then("the token is consumed")
    public void theTokenIsConsumed() {
        assertEquals(tokenCount-1,customerAccount.getCustomerTokens().size());
    }

    @After
    public void removeUserBankAccounts() {
        //TODO: Remove customer and merchant bank accounts
    }
}
