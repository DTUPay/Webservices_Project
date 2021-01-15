package dto;

import models.Payload;

import javax.json.JsonObject;

public class CustomerServiceDTO extends Payload {
    public String merchantId;


    public CustomerServiceDTO(){

    }

    public CustomerServiceDTO(JsonObject jsonObject){
        //this.merchantId = jsonObject.getString("payload");
        System.out.println(jsonObject);
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }


}
