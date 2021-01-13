package cucumber_glue;

import dtupay.CustomerRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.quarkus.test.junit.QuarkusTest;
import models.Customer;
import org.junit.Assert;

@QuarkusTest
public class resources {
    private CustomerRepository repository;
    private Customer customer;
    private Exception exception;

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
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("a customer with name {string} {string} and CPR {string} exists in the repository")
    public void a_customer_with_name_and_cpr_exists_in_the_repository(String string, String string2, String string3) {
        Assert.assertEquals(repository.getCustomer(string3), customer);
    }

    @When("a customer with CPR {string} is removed from the repository")
    public void a_customer_with_cpr_is_removed_from_the_repository(String string) {
        try {
            repository.deleteCustomer(string);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("a customer with CPR {string} does not exist in the repository")
    public void a_customer_with_cpr_does_not_exist_in_the_repository(String string) {
        Assert.assertNull(repository.getCustomer(string));
    }
}
