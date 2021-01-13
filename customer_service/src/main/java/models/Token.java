/*
@author Oliver O. Nielsen & Bjørn Wilting
 */

package models;
import java.util.Date;
import java.util.UUID;

public class Token {
    private UUID tokenID;
    private Date expiryDate;
    private int customerID;

    public UUID getTokenID() {
        return tokenID;
    }

    public void setTokenID(int tokenID) {
        this.tokenID = UUID.randomUUID();
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
}
