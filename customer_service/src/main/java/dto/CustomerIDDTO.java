package dto;
// @author: Oliver O. Nielsen
import models.Payload;

import java.util.UUID;

public class CustomerIDDTO extends Payload {
    private UUID customerID;

    public UUID getCustomerID() {
        return customerID;
    }

    public void setCustomerID(UUID customerID) {
        this.customerID = customerID;
    }
}
