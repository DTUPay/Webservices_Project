/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen
 */

package dtupay;


import brokers.CustomerBroker;
import exceptions.CustomerException;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import models.Customer;

import java.util.List;
import java.util.UUID;


/**
 * @author Mikkel Rosenfeldt Anderson & Benjamin Eriksen & Rubatharisan Thirumathyam
 */
@QuarkusMain
public class CustomerService {
    private static CustomerService instance = new CustomerService();
    CustomerBroker broker;
    ICustomerRepository customerRepository = new CustomerRepository();

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

    public UUID registerCustomer(Customer customer) throws CustomerException {
        if(customer.getFirstName() == null || customer.getFirstName().isEmpty() || customer.getLastName() == null || customer.getLastName().isEmpty() || customer.getAccountID() == null || customer.getAccountID().isEmpty()){
            throw new CustomerException("Missing fields for creating a customer");
        }

        UUID customerID;

        while(true) {
            customerID = UUID.randomUUID();
            if(!customerRepository.hasCustomer(customerID)){
                break;
            }
        }

        customer.setCustomerID(customerID);
        customerRepository.addCustomer(customer);
        return customer.getCustomerID();
    }

    public void removeCustomer(UUID customerID) throws CustomerException {
        if (customerRepository.hasCustomer(customerID)) {
            customerRepository.removeCustomer(customerID);
        }
        else throw new CustomerException("Customer with given customerID doesn't exist");
    }

    public Customer getCustomer(UUID customerID) throws CustomerException {
        if (customerRepository.hasCustomer(customerID)) {
            return customerRepository.getCustomer(customerID);
        }
        else throw new CustomerException("Customer with given customerID doesn't exist");
    }

    public boolean hasCustomer(UUID customerID) {
        return customerRepository.hasCustomer(customerID);
    }

    // Adds tokens to Customer
    public void addTokens(UUID customerID, List<UUID> tokens) throws CustomerException {
        if(customerRepository.hasCustomer(customerID)){
            Customer customer = customerRepository.getCustomer(customerID);
            customer.addTokens(tokens);
        } else {
            throw new CustomerException("Customer with given customerID doesn't exist");
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
    public boolean canRequestTokens(UUID customerID, int amount) throws CustomerException {

        if (!customerRepository.hasCustomer(customerID)){
            throw new CustomerException("Customer does not exist");
        }

        System.out.println(customerID.toString());
        List<UUID> tokens = customerRepository.getCustomer(customerID).getTokenIDs();
        System.out.println(tokens);
        int numTokens = tokens.size();
        if (numTokens > 1) throw new CustomerException("Customer with given customerID still has " + numTokens + " left."); // Should have spent all tokens
        if (tokens.size() + amount > 6) throw new CustomerException("Customer token request would exceed more than 6 tokens");
        return true;
    }
}
