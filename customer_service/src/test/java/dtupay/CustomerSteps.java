/*
@author Oliver O. Nielsen & Bjørn Wilting
 */

package dtupay;

import exceptions.CustomerException;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.Customer;
import models.Merchant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;


public class CustomerSteps {
    private CustomerService service;
    private Customer customer;
    private CustomerException exception;

    @Before
    public void initialize_repository(){
        service = new CustomerService();
    }

    @Given("a new customer with name {string} {string} and CPR {string} that does not exist in the repository")
    public void aNewCustomerWithNameAndCPRThatDoesNotExistInTheRepository(String arg0, String arg1, String arg2) {
        customer = new Customer(arg0, arg1, arg2);
        try {
            service.getCustomer(arg2);
            fail();
        } catch (CustomerException e) {
            assertTrue(true);
        }
    }

    @Given("a new customer with name {string} {string} and CPR {string} that does exist in the repository")
    public void aNewCustomerWithNameAndCPRThatDoesExistInTheRepository(String arg0, String arg1, String arg2) {
        customer = new Customer(arg0, arg1, arg2);
        try {
            service.registerCustomer(customer);
            assertEquals(service.getCustomer(arg2), customer);
        } catch (CustomerException e) {
            fail();
        }
    }

    @Then("a customer with CPR {string} does not exist in the repository")
    public void a_customer_with_cpr_does_not_exist_in_the_repository(String cpr)  {
        assertFalse(service.hasCustomer(cpr));
    }

    @Then("an error message with {string} is thrown")
    public void an_error_message_with_is_thrown(String msg) {
        assertEquals(msg, this.exception.getMessage());
    }

    @Then("a customer with name {string} {string} and CPR {string} exists in the repository")
    public void a_customer_with_name_and_cpr_exists_in_the_repository(String firstname, String lastname, String cpr) throws CustomerException {
        assertEquals(service.getCustomer(cpr), customer);
    }

    @When("a customer with CPR {string} is removed from the repository")
    public void a_customer_with_cpr_is_removed_from_the_repository(String cpr) {
        try {
            service.removeCustomer(cpr);
        } catch (CustomerException e) {
            exception = e;
        }
    }

    @When("the new customer is added to the repository")
    public void theNewCustomerIsAddedToTheRepository() {
        try {
            service.registerCustomer(customer);
        } catch (CustomerException e) {
            exception = e;
        }
    }
}
