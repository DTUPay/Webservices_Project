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
    private UUID tokenID;
    private boolean canRequestTokens;


    @Before
    public void initialize_repository(){
        service = new CustomerService();
    }


    @Given("a new customer with name {string} {string} and accountID {string} that does not exist in the repository")
    public void aNewCustomerWithNameAndAccountIDThatDoesNotExistInTheRepository(String firstName, String lastName, String accountID) {
        customer = new Customer(firstName, lastName, accountID,null);
    }

    @Given("a new customer with name {string} {string} and accountID {string} that does exist in the repository")
    public void aNewCustomerWithNameAndAccountIDThatDoesExistInTheRepository(String firstName, String lastName, String accountID) {
        customer = new Customer(firstName, lastName, accountID, null);
        try {
            service.registerCustomer(customer);
            UUID customerID = customer.getCustomerID();
            assertNotNull(customerID);
            assertEquals(service.getCustomer(customerID), customer);
        } catch (CustomerException e) {
            fail();
        }
    }

    @Then("a customer with accountID {string} does not exist in the repository")
    public void a_customer_with_accountID_does_not_exist_in_the_repository(String accountID)  {
        assertFalse(service.hasCustomer(customer.getCustomerID()));
    }

    @Then("an error message with {string} is thrown")
    public void an_error_message_with_is_thrown(String msg) {
        assertEquals(msg, this.exception.getMessage());
    }

    @Then("the token is removed from the customers token")
    public void theTokenIsRemovedFromTheCustomersToken() throws CustomerException {
        assertNull(this.exception);
        assertFalse(service.getCustomer(customer.getCustomerID()).getTokenIDs().contains(this.tokenID));
    }

    @Then("the customer exists in the repository")
    public void theCustomerExistsInTheRepository() {
        try {
            assertEquals(service.getCustomer(customer.getCustomerID()), customer);
        } catch (CustomerException e) {
            fail();
        }
    }

    @When("a customer with accountID {string} is removed from the repository")
    public void a_customer_with_customerID_is_removed_from_the_repository(String accountID) {
        try {
            service.removeCustomer(customer.getCustomerID());
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

    @Given("a random customerID that does not exist")
    public void aInvalidCustomerID() {
        try {
            service.getCustomer(UUID.randomUUID());
        } catch(CustomerException e){
            exception = e;
        }
    }

    @When("he requests more tokens")
    public void heRequestsMoreTokens() {
        try {
            canRequestTokens = service.canRequestTokens(customer.getCustomerID(), 1);
        } catch (CustomerException e) {
            this.exception = e;
        }
    }

    @And("has {int} unused token")
    public void hasUnusedToken(int arg0) {
        List<UUID> mocked_tokenIDs = new ArrayList<>();
        for (int i = 0; i < arg0; i++) {
            mocked_tokenIDs.add(UUID.randomUUID());
        }
        this.customer.setTokenIDs(mocked_tokenIDs);
    }

    @Then("his request is successful")
    public void hisRequestIsSuccessful() {
        assertTrue(canRequestTokens);
    }
}
