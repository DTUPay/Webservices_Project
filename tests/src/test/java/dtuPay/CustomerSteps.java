package dtuPay;

/*
@authors: Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin
 */
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.Customer;
import org.junit.Assert;
import servants.CustomerServant;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class CustomerSteps {
    private Customer customer = new Customer();
    private UUID customerID;
    private Exception exception;
    private CustomerServant customerAccount;


    @Before
    public void resetCustomerId(){
        this.customerID = null;
    }

    @Given("the customer have a bank account")
    public void theCustomerHaveABankAccount() {
        customer.setAccountNumber("some-bank-account-number");
    }

    @When("the customer requests a DTUPay account")
    public void theCustomerRequestsADTUPayAccount() {
        try {
            customerAccount = new CustomerServant(null);
            this.customerID = customerAccount.registerCustomer(customer);
        } catch (Exception e) {
            System.out.print(e.toString());
            exception = e;
        }

    }

    @Then("the customer posses a DTUPay account")
    public void theCustomerPossesADTUPayAccount() {
        Assert.assertNotNull(this.customerID);
        Assert.assertNull(exception);
    }

    @Given("the customer have no bank account")
    public void theCustomerHaveNoBankAccount() {
        customer.setAccountNumber(null);
        customer.setFirstName("my-firstname");
        customer.setLastName("my-lastname");
    }

    @Then("the customer will not be provided a DTUPay account")
    public void theCustomerWillNotBeProvidedADTUPayAccount() {
        Assert.assertNull(this.customerID);
        Assert.assertNotNull(exception);
    }

    @Given("the customer have no lastname")
    public void theCustomerHaveNoLastname() {
        customer.setAccountNumber("my-bank-account-number");
        customer.setFirstName("my-firstname");
        customer.setLastName(null);
    }

    @Given("the customer have no firstname")
    public void theCustomerHaveNoFirstname() {
        customer.setAccountNumber("my-bank-account-number");
        customer.setLastName("my-lastname");
        customer.setFirstName(null);
    }

    @Given("a customer with bank account, firstname and lastname")
    public void aCustomerWithBankAccountFirstnameAndLastname() {
        customer.setAccountNumber("my-bank-account-number");
        customer.setFirstName("my-firstname");
        customer.setLastName("my-lastname");
    }

    @And("the error {string} is thrown")
    public void theErrorIsThrown(String arg0) {
        assertEquals(arg0,exception.getMessage());
    }
}
