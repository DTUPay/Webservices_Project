/*
@author Oliver O. Nielsen & Bjørn Wilting & Benjamin Eriksen
 */

package dtupay;

import models.Customer;

public interface IReportingRepository {

    void addCustomer(Customer customer);

    Customer getCustomer(String cpr);

    void removeCustomer(String cpr);

    boolean hasCustomer(String cpr);
}