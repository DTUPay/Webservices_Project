package dtupay;

import dtu.ws.fastmoney.*;
import exceptions.BankException;
import exceptions.PaymentException;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.Customer;
import models.Merchant;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * @author Mikkel Rosenfeldt Anderson & Laura
 */
public class SoapSteps {
    PaymentService service = new PaymentService();
    User bankCustomer;
    User bankMerchant;
    String customerBankAccount;
    String merchantBankAccount;

    Account account;
    String accountNumber = "";
    int balance = 0;
    int fetchedBalance = 0;
    String errorMessage;
    BankService bankService = new BankServiceService().getBankServicePort();
    Exception exception;

    @Before()
    public void createAccounts() throws BankServiceException_Exception {
        bankCustomer = new User();

        bankMerchant = new User();
        bankMerchant.setFirstName("Mads");
        bankMerchant.setLastName("Madsen");
        bankMerchant.setCprNumber("140567-0001");
        merchantBankAccount = bankService.createAccountWithBalance(bankMerchant,new BigDecimal(1000));
    }

    @After()
    public void tearDown() throws BankServiceException_Exception {
        bankService.retireAccount(customerBankAccount);
        bankService.retireAccount(merchantBankAccount);
    }

    @Then("the balance should be {int} kroners")
    public void theBalanceShouldBeKroners(int arg0) {
        assertEquals(arg0,fetchedBalance);
    }

    @When("the customer is created")
    public void theCustomerIsCreated() {
        try {
            customerBankAccount = bankService.createAccountWithBalance(bankCustomer,new BigDecimal(balance));
        } catch (BankServiceException_Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @Given("a customer with name {string} {string} and CPR {string} with a balance of {int} kroners")
    public void aCustomerWithNameAndCPRWithABalanceOfKroners(String arg0, String arg1, String arg2, int arg3) {
        bankCustomer.setFirstName(arg0);
        bankCustomer.setLastName(arg1);
        bankCustomer.setCprNumber(arg2);
        balance = arg3;
    }

    @And("the account is fetched")
    public void theAccountIsFetched() {
        try {
            account = bankService.getAccount(customerBankAccount);
        } catch (Exception e) {
            exception = e;
            e.printStackTrace();
        }
        fetchedBalance = account.getBalance().intValue();
    }
}
