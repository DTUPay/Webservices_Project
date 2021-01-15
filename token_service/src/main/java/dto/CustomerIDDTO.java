package dto;

import models.Payload;

public class CustomerIDDTO extends Payload {
    public String CustomerID;

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }
}
