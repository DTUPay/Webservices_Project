/*
@author Oliver O. Nielsen & Bjørn Wilting
 */

package models;

public class Merchant {
    private String name;
    private String MerchantID;

    public Merchant(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMerchantID() {
        return MerchantID;
    }

    public void setMerchantID(String merchantID) {
        this.MerchantID = merchantID;
    }
}
