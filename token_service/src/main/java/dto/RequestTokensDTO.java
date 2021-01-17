package dto;

import java.util.UUID;

public class RequestTokensDTO {
    private UUID customerID;
    private int amount;

    public UUID getCustomerId() {
        return customerID;
    }

    public void setCustomerId(UUID customerId) {
        this.customerID = customerId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
