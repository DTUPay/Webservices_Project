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
    private String CPRNumber;
    private List<UUID> tokenIDs;

    public Customer(){

    }
    public Customer(String firstName, String lastName, String CPR){
        this.firstName = firstName;
        this.lastName = lastName;
        this.CPRNumber = CPR;
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

    public String getCPRNumber() {
        return CPRNumber;
    }

    public void setCPRNumber(String CPRNumber) {
        this.CPRNumber = CPRNumber;
    }

    public List<UUID> getTokenIDs() {
        return tokenIDs;
    }

    public void setTokenIDs(List<UUID> tokenIDs) {
        this.tokenIDs = tokenIDs;
    }
}
