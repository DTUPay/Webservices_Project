package servants;


import java.util.HashMap;
import java.util.UUID;

public class ManagementServant {
    public UUID registerCustomer(Customer customer) throws Exception {
        RestCommunicator dtuPay = new RestCommunicator(RestCommunicator.Service.MANAGEMENT);
        Object object = dtuPay.post(customer, "/customer", 201);
        HashMap<String, String> customerObject = (HashMap<String, String>) object;
        return UUID.fromString(customerObject.get("customerID"));
    }

    public UUID registerMerchant(Merchant merchant) throws Exception {
        RestCommunicator dtuPay = new RestCommunicator(RestCommunicator.Service.MANAGEMENT);
        Object object = dtuPay.post(merchant, "/merchant", 201);
        HashMap<String, String> customerObject = (HashMap<String, String>) object;
        return UUID.fromString(customerObject.get("merchantID"));
    }
}
