/*
@author Benjamin Eriksen
 */

package dtupay;

import models.Customer;

import java.util.HashMap;
public class CustomerRepository implements ICustomerRepository {

    HashMap<String, Customer> customers = new HashMap<>();

    @Override
    public void addCustomer(Customer customer) {
        customers.put(customer.getCpr(), customer);
    }

    @Override
    public Customer getCustomer(String cpr)  {
        return customers.get(cpr);
    }

    @Override
    public void removeCustomer(String cpr) {
        customers.remove(cpr);
    }

    @Override
    public boolean hasCustomer(String cpr) {
        return customers.containsKey(cpr);
    }

}
