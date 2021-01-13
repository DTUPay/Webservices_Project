package models;

public class Merchant {
    private int MerchantID;
    private String Name;
    private String CVR;

    public Merchant(){

    }

    public int getMerchantID() {
        return MerchantID;
    }

    public void setMerchantID(int merchantID) {
        MerchantID = merchantID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCVR() {
        return CVR;
    }

    public void setCVR(String CVR) {
        this.CVR = CVR;
    }
}
