package dtuPay;

import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.Customer;
import models.Merchant;
import servants.*;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.*;

public class PaymentSteps {
    private CustomerServant customerAccount;
    private MerchantServant merchantAccount;
    private ManagementServant accountManagement;
    private Customer customer;
    private Merchant merchant;
    private Exception exception;
    private int tokenCount;
    private BankServiceService service;
    private BankService bank;
    private UUID usableToken;
    private UUID paymentID;
    private boolean paymentRefunded;

    @Before
    public void CreateUsersInBank() throws BankServiceException_Exception {
        service = new dtu.ws.fastmoney.BankServiceService();
        bank = service.getBankServicePort();

        System.out.println("Hello world?");

        //TODO: Create customer and merchant bank accounts
        //Customer: Jens Jensen, 121012-xxxx, 100 DKK
        customer = new Customer("Jens", "Jensen", "121012-xxxx", 100);
        dtu.ws.fastmoney.User bankCustomer = new dtu.ws.fastmoney.User();
        bankCustomer.setFirstName("Jens");
        bankCustomer.setLastName("Jensen");
        bankCustomer.setCprNumber("121514-0001");
        try {
            dtu.ws.fastmoney.Account customerAccount = bank.getAccountByCprNumber(bankCustomer.getCprNumber());
            bank.retireAccount(customerAccount.getId());
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        System.out.println("Creating customer bank account...");
        String customerBankAccount = bank.createAccountWithBalance(bankCustomer, new BigDecimal(100));
        System.out.println("CustomerBankAccount: " + customerBankAccount);
        customer.setAccountNumber(customerBankAccount);
        System.out.println("done");
        //Merchant: Mads Madsen, 140467-xxxx, 1000 DKK
        merchant = new Merchant("Mads Madsen", "140467", 1000);
        User bankMerchant = new User();
        bankMerchant.setFirstName("Mads");
        bankMerchant.setLastName("Madsen");
        bankMerchant.setCprNumber("140567-0001");


        try {
            Account merchantAccount = bank.getAccountByCprNumber(bankMerchant.getCprNumber());
            bank.retireAccount(merchantAccount.getId());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println("Creating merchant bank account...");
        String merchantBankAccount = bank.createAccountWithBalance(bankMerchant, new BigDecimal(1000));
        System.out.println("MerchantBankAccount: " + merchantBankAccount);
        merchant.setAccountNumber(merchantBankAccount);
        System.out.println("done");

        //TODO: Store account numbers
        accountManagement = new ManagementServant();

    }

    @Given("the customer has the paymentID of his last payment")
    public void theCustomerHasThePaymentIDOfHisLastPayment() {
        assertNotNull(paymentID);
    }

    @Given("the customer has {int} tokens")
    public void theCustomerHasTokens(int arg0) {
        assertEquals(customerAccount.getCustomerTokens().size(), 0);
        try {
            System.out.println("Requesting customer tokens");
            customerAccount.requestTokens(customerAccount.getID(), arg0);
            tokenCount = customerAccount.getCustomerTokens().size();
            System.out.println(customerAccount.getCustomerTokens().size());
            assertEquals(tokenCount, arg0);
            System.out.println("done");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Given("a customer has a bank account provided by the bank with a balance of {int} DKK")
    public void aCustomerHasABankAccountProvidedByTheBankWithABalanceOfDkk(int arg0) throws BankServiceException_Exception {
        assertEquals(bank.getAccount(customer.getAccountNumber()).getBalance(), BigDecimal.valueOf(arg0));
    }

    @Given("the customer is registered with DTU Pay")
    public void theCustomerIsRegisteredWithDTUPay() throws Exception {
        System.out.println("Registering customer on DTU Pay");
        customerAccount = new CustomerServant(accountManagement.registerCustomer(customer));
        System.out.println("done");
    }

    @Given("a merchant has a bank account provided by the bank with a balance of {int} DKK")
    public void aMerchantWithNameAndCprNumberAndBalanceDKKHasABankAccount(int arg0) throws BankServiceException_Exception {
        assertEquals(bank.getAccount(merchant.getAccountNumber()).getBalance(), BigDecimal.valueOf(arg0));
    }

    @Given("the merchant is registered with DTU Pay")
    public void theMerchantIsRegisteredWithDTUPay() throws Exception {
        System.out.println("Registering customer on DTU Pay");
        merchantAccount = new MerchantServant(accountManagement.registerMerchant(merchant));
        System.out.println("done");
    }

    @Given("a successful payment of {int} DKK has been made to the merchant by the customer")
    public void aPaymentOfDKKHasBeenMadeToTheMerchantByTheCustomer(int arg0) throws Exception {
        paymentID = merchantAccount.requestPayment(arg0, merchantAccount.getId(), customerAccount.selectToken());
        assertNotNull(paymentID);
    }

    @Given("the customer has no tokens")
    public void theCustomerHasNoTokens() {
        assertTrue(customerAccount.getCustomerTokens().size() == 0);
    }

    @Given("the customer already has refunded a given payment")
    public void theCustomerAlreadyHasRefundedAGivenPayment() {
        try {
            paymentRefunded = customerAccount.requestRefund(customerAccount.selectToken(), paymentID);
        } catch (Exception e) {
            fail();
        }
    }

    @When("the customer request to see his account balance")
    public void theCustomerRequestToSeeHisAccountBalance() throws BankServiceException_Exception {
        BigDecimal customerBalance = bank.getAccount(customer.getAccountNumber()).getBalance();
        customer.setBalance(customerBalance.doubleValue());
        System.out.println("New balance of customer: " + customer.getBalance());
    }

    @When("the merchant request to see his account balance")
    public void theMerchantRequestToSeeHisAccountBalance() throws BankServiceException_Exception {
        BigDecimal merchantBalance = bank.getAccount(merchant.getAccountNumber()).getBalance();
        merchant.setBalance(merchantBalance.doubleValue());
        System.out.println("New balance of merchant: " + merchant.getBalance());
    }

    @When("the customer selects a token")
    public void theCustomerSelectsAToken() {
        try {
            this.usableToken = customerAccount.selectToken();
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("the merchant authorizes a payment with the customers token and an amount of {int} DKK")
    public void theMerchantAuthorizesAPaymentWithTheCustomersTokenAndAnAmountOfDKK(int arg0) {
        try {
            paymentID = merchantAccount.requestPayment(arg0, merchantAccount.getId(), this.usableToken);
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("the customer request to have the payment refunded")
    public void theCustomerRequestToHaveThePaymentRefunded() {
        try {
            paymentRefunded = customerAccount.requestRefund(customerAccount.selectToken(), paymentID);
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("the customer provides an invalid token")
    public void theCustomerProvidesAnInvalidToken() {
        usableToken = UUID.randomUUID();
    }

    @When("the customer requests {int} new tokens")
    public void the_customer_requests_new_tokens(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        try {
            System.out.println("Requesting customer tokens");
            customerAccount.requestTokens(customerAccount.getID(), int1);
            System.out.println("done");
        } catch (Exception e) {
            //e.printStackTrace();
            //fail();
        }
    }

    @Then("the payment succeeds")
    public void thePaymentSucceeds() {
        assertNotNull(paymentID);
    }

    @Then("the customer has {int} DKK in his account")
    public void theCustomerHasDKKInHisAccount(int arg0) {
        assertEquals(arg0, customer.getBalance(), 0);
    }

    @Then("the merchant has {int} DKK in his account")
    public void theMerchantHasDKKInHisAccount(int arg0) {
        assertEquals(arg0, merchant.getBalance(), 0);
    }

    @Then("the token is consumed")
    public void theTokenIsConsumed() {
        assertEquals(tokenCount - 1, customerAccount.getCustomerTokens().size());
    }

    @Then("the payment is refunded")
    public void thePaymentIsRefunded() {
        assertTrue(paymentRefunded);
    }

    @Then("the payment fails")
    public void thePaymentFails() {
        assertNull(paymentID);
    }

    @Then("the error message is {string}")
    public void theErrorMessageIs(String arg0) {
        assertEquals(arg0, exception.getMessage());
    }

    @Then("the refunding fails")
    public void theRefundingFails() {
        assertFalse(paymentRefunded);
    }

    @Then("the customer possesses {int} tokens")
    public void the_customer_possesses_tokens(int int1) {
        int tokens = customerAccount.getCustomerTokens().size();
        assertEquals(tokens, int1);
    }

    @After
    public void removeUserBankAccounts() throws BankServiceException_Exception {
        bank.retireAccount(customer.getAccountNumber());
        bank.retireAccount(merchant.getAccountNumber());
    }
}
