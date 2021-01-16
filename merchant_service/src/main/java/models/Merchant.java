package models;

public class Merchant extends Payload{
    private String name;
    private String merchantID;

    public Merchant(String name, String merchantID){
        this.merchantID = merchantID;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getMerchantID() {
        return merchantID;
    }

}
