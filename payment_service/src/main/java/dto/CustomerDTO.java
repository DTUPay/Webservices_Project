package dto;
// @author: Rubatharisan Thirumathyam & Oliver O. Nielsen
import models.Customer;
import models.Payload;

import java.util.UUID;

public class CustomerDTO extends Payload {
    private String firstName;
    private String lastName;
    private UUID customerID;
    private String accountNumber;

    public CustomerDTO(){

    }

    public CustomerDTO(Customer customer){
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.customerID = customer.getCustomerID();
        this.accountNumber = customer.getAccountID();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UUID getCustomerID() {
        return customerID;
    }

    public void setCustomerID(UUID customerID) {
        this.customerID = customerID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
