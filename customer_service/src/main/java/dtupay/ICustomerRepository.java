/*
@author Oliver O. Nielsen & Bjørn Wilting & Benjamin Eriksen
 */
// @author: Benjamin Eriksen
package dtupay;

import models.Customer;

import java.util.UUID;

public interface ICustomerRepository {

    void addCustomer(Customer customer);

    Customer getCustomer(UUID customerID);

    void removeCustomer(UUID customerID);

    boolean hasCustomer(UUID customerID);
}
