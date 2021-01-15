package servants;

public class Merchant {
    private String name;
    private String cpr;
    private int balance;

    public Merchant(String name, String cpr, int balance) {
        this.name = name;
        this.cpr = cpr;
        this.balance = balance;
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
