package models;
import java.util.Date;
import java.util.UUID;

public class Token {
    private UUID TokenID;
    private Date ExpiryDate;
    private UUID CustomerID;

    public UUID getTokenID() {
        return TokenID;
    }

    public void setTokenID(UUID tokenID) {
        TokenID = UUID.randomUUID();
    }

    public Date getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        ExpiryDate = expiryDate;
    }

    public UUID getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(UUID customerID) {
        CustomerID = customerID;
    }
}
