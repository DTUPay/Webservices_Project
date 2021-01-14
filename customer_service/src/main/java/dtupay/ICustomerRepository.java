/*
@author Oliver O. Nielsen & Bjørn Wilting
 */

package dtupay;

import models.Customer;

public interface ICustomerRepository {

    void registerCustomer(Customer customer) throws Exception;

    void deleteCustomer(String cpr) throws Exception;

    Customer getCustomer(String cpr);

}
