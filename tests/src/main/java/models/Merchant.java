package models;

public class Merchant {
    private String name;
    private String cpr;
    private double balance;
    private String accountNumber;

    public Merchant(String name, String cpr, double balance) {
        this.name = name;
        this.cpr = cpr;
        this.balance = balance;
    }

    public Merchant(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
