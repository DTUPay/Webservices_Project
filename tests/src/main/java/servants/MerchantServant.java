package servants;

/**
 * @author Laura Hansen s184234
 */
public class MerchantServant {
    private String name;
    private String cpr;
    private int balance;
    private String id;

    public MerchantServant(String id) {
        this.id = id;
    }

    public MerchantServant(String name, String cpr, int balance) {
        this.name = name;
        this.cpr = cpr;
        this.balance = balance;
    }

    public String getID() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public void setID(String id) {
        this.id = id;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }


    public void requestPayment(int amount) {
        RestCommunicator communicator = new RestCommunicator(RestCommunicator.Service.MERCHANT);
        String path = "/payment";
        // paymentrequest object m. merchant ID and payment amount
//        String url = communicator.post(amount, path);
    }

    public void generateReport() {

    }
}
