package servants;

import java.util.Date;

//Björn Wilting s184214
public class Token {
    private String tokenID;
    private Date expiryDate;
    private String customerID;

    public Token(String tokenID, Date expiryDate, String customerID) {
        this.tokenID = tokenID;
        this.expiryDate = expiryDate;
        this.customerID = customerID;
    }
}
