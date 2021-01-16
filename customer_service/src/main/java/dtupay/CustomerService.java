/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen
 */

package dtupay;


import brokers.CustomerBroker;
import com.google.gson.Gson;
import exceptions.CustomerException;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import models.Customer;

import java.util.UUID;

@QuarkusMain
public class CustomerService {
    CustomerBroker broker;
    ICustomerRepository customerRepository = new CustomerRepository();
    private static CustomerService instance = new CustomerService();

    Gson gson = new Gson();

    public CustomerService() {
        broker = new CustomerBroker(this);
    }

    public static CustomerService getInstance(){
        return instance;
    }

    public static void main(String[] args) {
        CustomerService service = CustomerService.getInstance();
        Quarkus.run();
    }

    public void registerCustomer(Customer customer) throws CustomerException {
        if (!customerRepository.hasCustomer(customer.getCustomerID())) {
            customerRepository.addCustomer(customer);
        }
        else throw new CustomerException("Customer with CPR: " + customer.getCustomerID() + " already exists");
    }

    public void removeCustomer(String customerID) throws CustomerException {
        if (customerRepository.hasCustomer(customerID)) {
            customerRepository.removeCustomer(customerID);
        }
        else throw new CustomerException("Customer with CPR: " + customerID + " doesn't exist");
    }

    public Customer getCustomer(String customerID) throws CustomerException {
        if (customerRepository.hasCustomer(customerID)) {
            return customerRepository.getCustomer(customerID);
        }
        else throw new CustomerException("Customer with CPR: " + customerID + " doesn't exist");
    }

    public boolean hasCustomer(String customerID) {
        return customerRepository.hasCustomer(customerID);
    }

    public UUID getUnusedToken(String customerID) throws CustomerException {
        if (customerRepository.hasCustomer(customerID)) {
            Customer customer = customerRepository.getCustomer(customerID);
            UUID tokenID = customer.getTokenIDs().get(customer.getTokenIDs().size() - 1);
            if (tokenID != null) {
                return tokenID;
            } else {
                throw new CustomerException("No more tokens left");
            }
        } else {
            throw new CustomerException("Customer with CPR: " + customerID + " doesn't exist");
        }

    }
}
