package dto;

import models.Payload;

public class MerchantDTO extends Payload {
    private String merchantID;
    private String name;

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
