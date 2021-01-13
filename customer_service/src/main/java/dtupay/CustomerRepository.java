package dtupay;

import java.util.HashMap;
public class CustomerRepository implements ICustomerRepository {

    HashMap<String, dtupay.Customer> customers = new HashMap<>();

    @Override
    public void registerCustomer(dtupay.Customer customer) throws Exception {
        customers.put(customer.getCPRNumber(), customer);
    }

    @Override
    public void deleteCustomer(String cpr) throws Exception {
        customers.remove(cpr);
    }
}
