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


/**
 * @author Mikkel & Benjamin
 */
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

    public UUID registerCustomer(Customer customer) throws CustomerException {
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

    public UUID getUnusedToken(UUID customerID) throws CustomerException {
        if (customerRepository.hasCustomer(customerID)) {
            try {
                return customerRepository.getCustomer(customerID).getTokenIDs().remove(0);
            } catch (IndexOutOfBoundsException e) {
                throw new CustomerException("No more tokens left");
            }
        } else {
            throw new CustomerException("Customer with given customerID doesn't exist");
        }
    }
}
