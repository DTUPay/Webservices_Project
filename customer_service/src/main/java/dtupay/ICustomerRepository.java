/*
@author Oliver O. Nielsen & Bjørn Wilting & Benjamin Eriksen
 */

package dtupay;

import exceptions.CustomerException;
import models.Customer;

public interface ICustomerRepository {

    void addCustomer(Customer customer) throws CustomerException;

    Customer getCustomer(String cpr) throws CustomerException;

    void removeCustomer(String cpr) throws CustomerException;

    boolean hasCustomer(String cpr);
}
