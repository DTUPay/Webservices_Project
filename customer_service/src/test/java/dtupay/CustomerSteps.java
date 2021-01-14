/*
@author Oliver O. Nielsen & Bjørn Wilting
 */

package dtupay;

import exceptions.CustomerException;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.Customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class CustomerSteps {
    private CustomerRepository repository;
    private Customer customer;
    private CustomerException exception;

    //TODO: Should act through the a service - not directly on the repository, as that would make changes in database system require changes to business logic.
    @Before
    public void initialize_repository(){
        repository = new CustomerRepository();
    }

    @Given("a new customer with name {string} {string} and CPR {string}")
    public void a_new_customer_with_name_and_cpr(String string, String string2, String string3) {
        customer = new Customer(string, string2, string3);
    }

    @When("the new customer is added to the repository")
    public void the_new_customer_is_added_to_the_repository() {
        try {
            repository.registerCustomer(customer);
        } catch (CustomerException e) {
            exception = e;
        }
    }

    @Then("a customer with name {string} {string} and CPR {string} exists in the repository")
    public void a_customer_with_name_and_cpr_exists_in_the_repository(String string, String string2, String string3) throws CustomerException {
        assertEquals(repository.getCustomer(string3), customer);
    }

    @When("a customer with CPR {string} is removed from the repository")
    public void a_customer_with_cpr_is_removed_from_the_repository(String string) {
        try {
            repository.deleteCustomer(string);
        } catch (CustomerException e) {
            exception = e;
        }
    }

    @Then("a customer with CPR {string} does not exist in the repository")
    public void a_customer_with_cpr_does_not_exist_in_the_repository(String string) throws CustomerException {
        assertNull(repository.getCustomer(string));
    }
}