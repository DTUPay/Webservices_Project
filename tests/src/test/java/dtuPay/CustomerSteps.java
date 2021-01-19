package dtuPay;

/*
@authors: Oliver O. Nielsen & Rubatharisan Thirumathyam
 */
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import models.Customer;
import servants.RestCommunicator;

import java.util.HashMap;
import java.util.UUID;

public class CustomerSteps {
    private Customer customer = new Customer();
    private UUID customerID;
    private Exception exception;


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
            RestCommunicator dtuPay = new RestCommunicator(RestCommunicator.Service.CUSTOMER);
            Object object = dtuPay.post(customer, "/customer", 201);
            HashMap<String, String> customerObject = (HashMap<String, String>) object;
            this.customerID = UUID.fromString(customerObject.get("customerID"));
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
}
