/*
@author Oliver O. Nielsen & Bjørn Wilting & Benjamin Eriksen
 */

package models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer {
    private String firstName;
    private String lastName;
    private String accountID;
    private UUID customerID;
    private List<UUID> tokenIDs;

    public Customer(){

    }
    public Customer(String firstName, String lastName, String accountID, UUID customerID){
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountID = accountID;
        this.customerID = customerID;
        this.tokenIDs = new ArrayList<UUID>();
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

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public UUID getCustomerID() {
        return customerID;
    }

    public void setCustomerID(UUID customerID) {
        this.customerID = customerID;
    }

    public List<UUID> getTokenIDs() {
        return tokenIDs;
    }

    public void setTokenIDs(List<UUID> tokenIDs) {
        this.tokenIDs = tokenIDs;
    }
}
