/*
@author Benjamin Eriksen
 */

package dtupay;

import exceptions.CustomerException;
import models.Customer;

import java.util.HashMap;
public class CustomerRepository implements ICustomerRepository {

    HashMap<String, Customer> customers = new HashMap<>();

    @Override
    public void addCustomer(Customer customer) throws CustomerException {
        if (customers.containsKey(customer.getCPRNumber())) {
            throw new CustomerException("Customer with CPR: "+ customer.getCPRNumber() + " already exists");
        }
        customers.put(customer.getCPRNumber(), customer);
    }

    @Override
    public Customer getCustomer(String cpr) throws CustomerException {
        if (customers.containsKey(cpr)) {
            throw new CustomerException("Customer with CPR: "+ cpr + " doesn't exist");
        }
        return customers.get(cpr);
    }

    @Override
    public void removeCustomer(String cpr) throws CustomerException {
        if (customers.containsKey(cpr)) {
            throw new CustomerException("Customer with CPR: "+ cpr + " doesn't exist");
        }
        customers.remove(cpr);
    }

    @Override
    public boolean hasCustomer(String cpr) {
        return customers.containsKey(cpr);
    }

}
