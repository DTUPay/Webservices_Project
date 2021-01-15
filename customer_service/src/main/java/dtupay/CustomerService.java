/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen
 */

package dtupay;


import brokers.CustomerBroker;
import exceptions.CustomerException;
import models.Customer;

public class CustomerService {
    ICustomerRepository customerRepository;
    CustomerBroker broker;
    public CustomerService() {
        broker = new CustomerBroker(this);

    }

    public void registerCustomer(Customer customer) throws CustomerException {
        if (!customerRepository.hasCustomer(customer.getCPRNumber())) {
            customerRepository.addCustomer(customer);
        }
        else throw new CustomerException("Customer with CPR: " + customer.getCPRNumber() + " already exists");
    }

    public void removeCustomer(String cpr) throws CustomerException {
        if (customerRepository.hasCustomer(cpr)) {
            customerRepository.removeCustomer(cpr);
        }
        else throw new CustomerException("Customer with CPR: " + cpr + " doesn't exist");
    }


    public Customer getCustomer(String cpr) throws CustomerException {
        if (customerRepository.hasCustomer(cpr)) {
            return customerRepository.getCustomer(cpr);
        }
        else throw new CustomerException("Customer with CPR: " + cpr + " doesn't exist");
    }

    public boolean hasCustomer(String cprNumber) {
        return customerRepository.hasCustomer(cprNumber);
    }

    }
