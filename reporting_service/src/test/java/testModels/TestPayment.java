package testModels;

import models.Payment;

import java.util.Date;
import java.util.UUID;

public class TestPayment extends Payment {

    /**
     * Create pending Payment
     *
     * @param merchantID
     * @param amount
     */
    public TestPayment(UUID merchantID, float amount) {
        super(merchantID, amount);
    }

    public void setDate(Date newDate){
        this.date = newDate;
    }
}
