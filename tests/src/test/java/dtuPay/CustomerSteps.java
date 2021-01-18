package dtuPay;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import servants.Customer;
import servants.CustomerServant;
import servants.RestCommunicator;

import java.util.HashMap;
import java.util.UUID;

public class CustomerSteps {
    private Customer customer = new Customer();
    private UUID customerID;
    private CustomerServant customerAccount;
    private Exception exception;
    private int expectedErrorCode;


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
            Object object = dtuPay.post(customer, "/customer", this.expectedErrorCode);
            if(this.expectedErrorCode == 201){
                HashMap<String, String> customerObject = (HashMap<String, String>) object;
                this.customerID = UUID.fromString(customerObject.get("customerID"));
            }
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
        expectedErrorCode = 400;
        customer.setAccountNumber(null);
        customer.setFirstName("my-firstname");
        customer.setLastName("my-lastname");
    }

    @Then("the customer will not be provided a DTUPay account")
    public void theCustomerWillNotBeProvidedADTUPayAccount() {
        Assert.assertNull(this.customerID);
        Assert.assertNull(exception);
    }

    @Given("the customer have no lastname")
    public void theCustomerHaveNoLastname() {
        expectedErrorCode = 400;
        customer.setAccountNumber("my-bank-account-number");
        customer.setFirstName("my-firstname");
        customer.setLastName(null);
    }

    @Given("the customer have no firstname")
    public void theCustomerHaveNoFirstname() {
        expectedErrorCode = 400;
        customer.setAccountNumber("my-bank-account-number");
        customer.setLastName("my-lastname");
        customer.setFirstName(null);
    }

    @Given("a customer with bank account, firstname and lastname")
    public void aCustomerWithBankAccountFirstnameAndLastname() {
        expectedErrorCode = 200;
        customer.setAccountNumber("my-bank-account-number");
        customer.setFirstName("my-firstname");
        customer.setLastName("my-lastname");
    }
}
