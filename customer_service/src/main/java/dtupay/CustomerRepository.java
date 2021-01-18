/*
@author Benjamin Eriksen
 */
package dtupay;
// @author: Benjamin Eriksen
import models.Customer;

import java.util.HashMap;
import java.util.UUID;

public class CustomerRepository implements ICustomerRepository {

    HashMap<UUID, Customer> customers = new HashMap<>();

    @Override
    public void addCustomer(Customer customer) {
        customers.put(customer.getCustomerID(), customer);
    }

    @Override
    public Customer getCustomer(UUID customerID)  {
        return customers.get(customerID);
    }

    @Override
    public void removeCustomer(UUID customerID) {
        customers.remove(customerID);
    }

    @Override
    public boolean hasCustomer(UUID customerID) {
        return customers.containsKey(customerID);
    }

}
