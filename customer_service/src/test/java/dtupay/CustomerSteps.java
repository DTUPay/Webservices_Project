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

import static org.junit.jupiter.api.Assertions.*;


public class CustomerSteps {
    private CustomerService service;
    private Customer customer;
    private CustomerException exception;

    @Before
    public void initialize_repository(){
        service = new CustomerService();
    }

    @Given("a new customer with name {string} {string} and CPR {string}")
    public void a_new_customer_with_name_and_cpr(String firstname, String lastname, String cpr) {
        customer = new Customer(firstname, lastname, cpr);
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

    @Then("a customer with CPR {string} does not exist in the repository")
    public void a_customer_with_cpr_does_not_exist_in_the_repository(String cpr)  {
        assertFalse(service.hasCustomer(cpr));
    }

    @And("the new customer is added to the repository")
    public void theNewCustomerIsAddedToTheRepository() {
        try {
            service.registerCustomer(customer);
        } catch (CustomerException e) {
            exception = e;
        }
    }

    @Then("an error message with {string} is thrown")
    public void an_error_message_with_is_thrown(String msg) {

        assertEquals(msg, this.exception.getMessage());
    }


}
