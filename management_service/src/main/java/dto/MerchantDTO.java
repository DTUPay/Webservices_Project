package dto;

import models.Payload;

public class MerchantDTO extends Payload {
    private String cvr;
    private String name;

    public String getCvr() {
        return cvr;
    }

    public void setCvr(String cvr) {
        this.cvr = cvr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
