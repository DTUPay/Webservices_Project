package dto;

import models.Payload;

public class MerchantIDDTO extends Payload {
    public String MerchantID;

    public String getMerchantID() {
        return MerchantID;
    }

    public void setMerchantID(String merchantID) {
        MerchantID = merchantID;
    }
}
