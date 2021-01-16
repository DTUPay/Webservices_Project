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

import java.util.List;
import java.util.UUID;


/**
 * @author Mikkel Rosenfeldt Anderson & Benjamin
 */
@QuarkusMain
public class CustomerService {
    private static CustomerService instance = new CustomerService();
    CustomerBroker broker;
    ICustomerRepository customerRepository = new CustomerRepository();
    Gson gson = new Gson();

    public CustomerService() {
        broker = new CustomerBroker(this);
    }

    public static void main(String[] args) {
        CustomerService service = CustomerService.getInstance();
        Quarkus.run();
    }

    public static CustomerService getInstance(){
        return instance;
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
            try {
                return customerRepository.getCustomer(customerID).getTokenIDs().remove(0);
            } catch (IndexOutOfBoundsException e) {
                throw new CustomerException("No more tokens left");
            }
        } else {
            throw new CustomerException("Customer with CPR: " + customerID + " doesn't exist");
        }
    }

    /**
     * The customer can request 1 to 5 tokens if he either has spent all tokens
     * (or it is the first time he requests tokens)
     * or has only one unused token left.
     * Overall, a customer can only have at most 6 unused tokens.
     * If the user has more than 1 unused token and he requests again a set of tokens, his request will be denied.
     *
     * @param customerID The customer ID
     *
     * @return true if customer can request tokens
     *
     * @throws CustomerException
     */
    public boolean canRequestTokens(String customerID) throws CustomerException {
        List<UUID> tokens = customerRepository.getCustomer(customerID).getTokenIDs();
        int numTokens = tokens.size();
        if (numTokens > 1) throw new CustomerException("Customer with ID: " + customerID + " still has " + numTokens + " left.");; // Should have spent all tokens
        return true;
    }
}
