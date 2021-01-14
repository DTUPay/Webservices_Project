/*
@author Oliver O. Nielsen & Bjørn Wilting
 */

package dtupay;

import exceptions.CustomerException;
import models.Customer;

public interface ICustomerRepository {

    void registerCustomer(Customer customer) throws CustomerException;

    void deleteCustomer(String cpr) throws CustomerException;

    Customer getCustomer(String cpr) throws CustomerException;

}
