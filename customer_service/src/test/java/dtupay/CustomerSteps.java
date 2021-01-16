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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CustomerSteps {
    private CustomerService service;
    private Customer customer;
    private CustomerException exception;
    private String tokenID;

    @Before
    public void initialize_repository(){
        service = new CustomerService();
    }

    @Given("a new customer with name {string} {string} and accountID {string} that does not exist in the repository")
    public void aNewCustomerWithNameAndAccountIDThatDoesNotExistInTheRepository(String firstName, String lastName, String accountID) {
        customer = new Customer(firstName, lastName, accountID);
        try {
            service.getCustomer(accountID);
            fail();
        } catch (CustomerException e) {
            assertTrue(true);
        }
    }

    @Given("a new customer with name {string} {string} and accountID {string} that does exist in the repository")
    public void aNewCustomerWithNameAndAccountIDThatDoesExistInTheRepository(String firstName, String lastName, String accountID) {
        customer = new Customer(firstName, lastName, accountID);
        try {
            service.registerCustomer(customer);
            assertEquals(service.getCustomer(accountID), customer);
        } catch (CustomerException e) {
            fail();
        }
    }

    @Then("a customer with accountID {string} does not exist in the repository")
    public void a_customer_with_accountID_does_not_exist_in_the_repository(String accountID)  {
        assertFalse(service.hasCustomer(accountID));
    }

    @Then("an error message with {string} is thrown")
    public void an_error_message_with_is_thrown(String msg) {
        assertEquals(msg, this.exception.getMessage());
    }

    @Then("a customer with name {string} {string} and accountID {string} exists in the repository")
    public void a_customer_with_name_and_accountID_exists_in_the_repository(String firstname, String lastname, String accountID) throws CustomerException {
        assertEquals(service.getCustomer(accountID), customer);
    }

    @When("a customer with customerID {string} is removed from the repository")
    public void a_customer_with_customerID_is_removed_from_the_repository(String accountID) {
        try {
            service.removeCustomer(accountID);
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

    @And("has a unused token")
    public void hasAUnusedToken() {
        List<UUID> mocked_tokenIDs = new ArrayList<>();
        mocked_tokenIDs.add(UUID.randomUUID());
        this.customer.setTokenIDs(mocked_tokenIDs);
    }

    @And("the customer request a unused token")
    public void theCustomerRequestAUnusedToken() {
        try {
            this.tokenID = service.getUnusedToken(customer.getCustomerID());
        } catch (CustomerException e) {
            this.exception = e;
        }
    }

    @Then("the token is removed from the customers token")
    public void theTokenIsRemovedFromTheCustomersToken() throws CustomerException {
        assertNull(this.exception);
        assertFalse(service.getCustomer(customer.getCustomerID()).getTokenIDs().contains(this.tokenID));
    }
}
