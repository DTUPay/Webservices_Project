package dtuPay;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;

import servants.*;
import dtu.ws.fastmoney.*;

import java.math.BigDecimal;

import static java.lang.System.exit;
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
    private BankServiceService service;
    private BankService bank;

    @Before
    public void CreateUsersInBank() throws BankServiceException_Exception {
        service  = new dtu.ws.fastmoney.BankServiceService();
        bank = service.getBankServicePort();

        System.out.println("Hello world?");

        //TODO: Create customer and merchant bank accounts
        //Customer: Jens Jensen, 121012-xxxx, 100 DKK
        customer = new Customer("Jens", "Jensen", "121012-xxxx", 100);
        User bankCustomer = new User();
        bankCustomer.setFirstName("Jens");
        bankCustomer.setLastName("Jensen");
        bankCustomer.setCprNumber("121514-0001");

        try {
            Account customerAccount = bank.getAccountByCprNumber(bankCustomer.getCprNumber());
            bank.retireAccount(customerAccount.getId());
        } catch(Exception e) {
            System.out.println(e.toString());
        }

        String customerBankAccount = bank.createAccountWithBalance(bankCustomer, new BigDecimal(100));
        customer.setAccountNumber(customerBankAccount);

        //Merchant: Mads Madsen, 140467-xxxx, 1000 DKK
        merchant = new Merchant("Mads Madsen", "140467", 1000);
        User bankMerchant = new User();
        bankMerchant.setFirstName("Mads");
        bankMerchant.setLastName("Madsen");
        bankMerchant.setCprNumber("140567-0001");


        try {
            Account merchantAccount = bank.getAccountByCprNumber(bankMerchant.getCprNumber());
            bank.retireAccount(merchantAccount.getId());
        } catch(Exception e) {
            System.out.println(e.toString());
        }

        String merchantBankAccount = bank.createAccountWithBalance(bankMerchant, new BigDecimal(1000));

        merchant.setAccountNumber(merchantBankAccount);

        //TODO: Store account numbers
        accountManagement = new ManagementServant();

    }

    @Given("the customer has {int} tokens")
    public void theCustomerHasTokens(int arg0) {
        assertEquals(customerAccount.getCustomerTokens().size(), 0);
        try {
            customerAccount.requestTokens(customerAccount.getID(),arg0);
            tokenCount = customerAccount.getCustomerTokens().size();
            System.out.println(customerAccount.getCustomerTokens().size());
            assertEquals(tokenCount, arg0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Given("a customer with name {string} {string} and cpr number {string} and balance {int} DKK has a bank account")
    public void aCustomerWithNameAndCprNumberAndBalanceDKKHasABankAccount(String arg0, String arg1, String arg2, int arg3) {
        //TODO: Assert that customer is registered with the bank
    }

    @Given("the customer is registered with DTU Pay")
    public void theCustomerIsRegisteredWithDTUPay() throws Exception {
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
    public void removeUserBankAccounts() throws BankServiceException_Exception {
        //TODO: Remove customer and merchant bank accounts
        bank.retireAccount(customer.getAccountNumber());
        bank.retireAccount(merchant.getAccountNumber());
    }
}
