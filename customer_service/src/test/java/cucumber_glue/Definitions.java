package cucumber_glue;

import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import dtupay.CustomerRepository;
import models.Customer;
import models.Token;
import org.junit.Assert;

import java.util.ArrayList;

public class Definitions {
    Customer customer;
    CustomerRepository repository;

    @Before
    public void initialize_repository(){
        repository = new CustomerRepository();
    }

    @Given("^a customer with cpr \"([^\"]*)\" exists$")
    public void a_customer_with_cpr_exists(String arg1) throws Exception {
        Customer c = new Customer(1, "Jens", "Jensen", arg1);
        repository.registerCustomer(c);
    }

    @When("^looking for a customer with cpr \"([^\"]*)\"$")
    public void looking_for_a_customer_with_cpr(String arg1) throws Exception {
        customer = repository.getCustomer(arg1);
    }

    @Then("^a unique customer with cpr \"([^\"]*)\" is found$")
    public void aUniqueCustomerWithCprIsFound(String cpr) throws Throwable {
        Assert.assertEquals(repository.getCustomer(cpr), customer);
    }

    @Given("^a customer with cpr \"([^\"]*)\" does not exist$")
    public void a_customer_with_cpr_does_not_exist(String arg1) throws Exception {
        // Write code here that turns the phrase above into concrete actions
        //throw new PendingException();
    }

    @Then("^no customer is found$")
    public void no_customer_is_found() throws Exception {
        // Write code here that turns the phrase above into concrete actions
        //throw new PendingException();
    }

}
