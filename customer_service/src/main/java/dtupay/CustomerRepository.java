package dtupay;

import exceptions.CustomerException;
import models.Customer;

import java.util.HashMap;
public class CustomerRepository implements ICustomerRepository {

    HashMap<String, Customer> customers = new HashMap<>();

    @Override
    public void registerCustomer(Customer customer) throws CustomerException {
        // TODO: Handle customer already exists
        customers.put(customer.getCPRNumber(), customer);
    }

    @Override
    public void deleteCustomer(String cpr) throws CustomerException {
        // TODO: Handle customer not found
        customers.remove(cpr);
    }

    @Override
    public Customer getCustomer(String cpr) throws CustomerException {
        // TODO: Handle customer not found
        return customers.get(cpr);
    }

}
