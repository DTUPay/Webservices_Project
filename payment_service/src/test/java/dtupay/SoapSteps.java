package dtupay;

import dtu.ws.fastmoney.Account;
import exceptions.PaymentException;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.Customer;


import static org.junit.Assert.*;

/**
 * @author Mikkel & Laura
 */
public class SoapSteps {
    PaymentRepository repo = new PaymentRepository();
    Customer customer = new Customer();
    Account account;
    String accountNumber = "";
    int balance = 0;
    int fetchedBalance = 0;
    String errorMessage;

    @After()
    public void tearDown(){
        try {
            repo.deleteAccount(customer.getCPRNumber());
        } catch (PaymentException e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("the balance should be {int} kroners")
    public void theBalanceShouldBeKroners(int arg0) {
        assertEquals(arg0,fetchedBalance);
    }

    @When("the customer is created")
    public void theCustomerIsCreated() {
        try {
            accountNumber = repo.createAccountWithBalance(customer,balance);
        } catch (PaymentException e) {
            errorMessage = e.getMessage();
        }
    }

    @Given("a customer with name {string} {string} and CPR {string} with a balance of {int} kroners")
    public void aCustomerWithNameAndCPRWithABalanceOfKroners(String arg0, String arg1, String arg2, int arg3) {
        customer.setFirstName(arg0);
        customer.setLastName(arg1);
        customer.setCPRNumber(arg2);
        balance = arg3;
    }

    @And("the account is fetched")
    public void theAccountIsFetched() {
        account = repo.getAccount(accountNumber);
        fetchedBalance = account.getBalance().intValue();
    }
}
