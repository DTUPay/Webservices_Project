package models;
import java.util.Date;
import java.util.UUID;

public class Token {
    private UUID TokenID;
    private Date ExpiryDate;
    private int CustomerID;

    public UUID getTokenID() {
        return TokenID;
    }

    public void setTokenID(int tokenID) {
        TokenID = UUID.randomUUID();
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
