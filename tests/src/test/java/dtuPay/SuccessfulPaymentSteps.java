package dtuPay;

import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import servants.*;

import java.math.BigDecimal;
import java.util.UUID;

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
    private UUID usableToken;

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
        } catch (Exception e) {
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
            customerAccount.requestTokens(customerAccount.getID(), arg0);
            tokenCount = customerAccount.getCustomerTokens().size();
            System.out.println(customerAccount.getCustomerTokens().size());
            assertEquals(tokenCount, arg0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Given("a customer has a bank account provided by the bank")
    public void aCustomerWithNameAndCprNumberAndBalanceDKKHasABankAccount() {
        assertNotNull(customer.getAccountNumber());
    }

    @Given("the customer is registered with DTU Pay")
    public void theCustomerIsRegisteredWithDTUPay() throws Exception {
        customerAccount = new CustomerServant(accountManagement.registerCustomer(customer));
    }

    @Given("a merchant has a bank account provided by the bank")
    public void aMerchantWithNameAndCprNumberAndBalanceDKKHasABankAccount() {
        assertNotNull(merchant.getAccountNumber());
    }

    @Given("the merchant is registered with DTU Pay")
    public void theMerchantIsRegisteredWithDTUPay() throws Exception {
        merchantAccount = new MerchantServant(accountManagement.registerMerchant(merchant));
    }

    @When("the customer request to see his account balance")
    public void theCustomerRequestToSeeHisAccountBalance() throws BankServiceException_Exception {
        BigDecimal customerBalance = bank.getAccount(customer.getAccountNumber()).getBalance();
        customer.setBalance(customerBalance.doubleValue());
    }

    @When("the merchant request to see his account balance")
    public void theMerchantRequestToSeeHisAccountBalance() throws BankServiceException_Exception {
        BigDecimal merchantBalance = bank.getAccount(merchant.getAccountNumber()).getBalance();
        merchant.setBalance(merchantBalance.doubleValue());
    }

    @Then("the payment succeeds")
    public void thePaymentSucceeds() {
        assertTrue(success);
    }

    @Then("the customer has {int} DKK in his account")
    public void theCustomerHasDKKInHisAccount(int arg0) {
        assertEquals(customer.getBalance(), arg0, 0);
    }

    @Then("the merchant has {int} DKK in his account")
    public void theMerchantHasDKKInHisAccount(int arg0) {
        assertEquals(merchant.getBalance(), arg0, 0);
    }

    @Then("the token is consumed")
    public void theTokenIsConsumed() {
        assertEquals(tokenCount - 1, customerAccount.getCustomerTokens().size());
    }

    @After
    public void removeUserBankAccounts() throws BankServiceException_Exception {
        bank.retireAccount(customer.getAccountNumber());
        bank.retireAccount(merchant.getAccountNumber());
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
            this.success = merchantAccount.requestPayment(arg0, merchantAccount.getId(), this.usableToken);
        } catch (Exception e) {
            exception = e;
        }
    }
}
