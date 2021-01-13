package dtupay;

import java.util.Date;

public class Token {
    private int TokenID;
    private Date ExpiryDate;
    private int CustomerID;

    public int getTokenID() {
        return TokenID;
    }

    public void setTokenID(int tokenID) {
        TokenID = tokenID;
    }

    public Date getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        ExpiryDate = expiryDate;
    }

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }
}
