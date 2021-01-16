package dto;

import models.Payload;

public class MerchantIDDTO extends Payload {
    public String merchantID;

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }
}
