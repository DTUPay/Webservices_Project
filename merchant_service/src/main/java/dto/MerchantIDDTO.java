package dto;

import models.Payload;

public class MerchantIDDTO extends Payload {
    public String cvr;

    public String getMerchantID() {
        return cvr;
    }

    public void setMerchantID(String merchantID) {
        this.cvr = merchantID;
    }
}
