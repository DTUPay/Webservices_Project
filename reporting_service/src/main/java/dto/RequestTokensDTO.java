package dto;

public class RequestTokensDTO {
    private String customerID;
    private int amount;

    public String getCustomerId() {
        return customerID;
    }

    public void setCustomerId(String customerId) {
        this.customerID = customerId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
