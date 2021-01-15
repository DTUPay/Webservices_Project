/*
@author Oliver O. Nielsen & Bjørn Wilting
 */

package models;

public class Merchant {
    private int merchantID;
    private String name;
    private String CVR;

    public Merchant(){

    }

    public int getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(int merchantID) {
        this.merchantID = merchantID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCVR() {
        return CVR;
    }

    public void setCVR(String CVR) {
        this.CVR = CVR;
    }
}
