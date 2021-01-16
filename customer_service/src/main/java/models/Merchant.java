/*
@author Oliver O. Nielsen & Bjørn Wilting
 */

package models;

public class Merchant {
    private String name;
    private String merchantID;

    public Merchant(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }
}
