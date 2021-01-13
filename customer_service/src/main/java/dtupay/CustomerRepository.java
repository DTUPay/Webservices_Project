package dtupay;

import models.Customer;

import java.util.HashMap;
public class CustomerRepository implements ICustomerRepository {

    HashMap<String, Customer> customers = new HashMap<>();

    @Override
    public void registerCustomer(Customer customer) throws Exception {
        customers.put(customer.getCPRNumber(), customer);
    }

    @Override
    public void deleteCustomer(String cpr) throws Exception {
        customers.remove(cpr);
    }

    @Override
    public Customer getCustomer(String cpr) throws Exception {
        return customers.get(cpr);
    }
}
