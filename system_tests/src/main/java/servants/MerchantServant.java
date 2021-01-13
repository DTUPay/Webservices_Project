package servants;

/**
 * @author Laura Hansen s184234
 */
public class MerchantServant {
    private String id;

    public MerchantServant(String id) {
        this.id = id;
    }

    public void requestPayment(int amount) {
        RestCommunicator communicator = new RestCommunicator(RestCommunicator.Service.MERCHANT.port);
        String path = RestCommunicator.Service.MERCHANT.port + "/Payment";
        // paymentrequest object m. merchant ID and payment amount
//        String url = communicator.post(amount, path);
    }

    public void generateReport() {

    }
}
