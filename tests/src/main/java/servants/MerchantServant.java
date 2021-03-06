package servants;

import models.Merchant;
import models.Payment;
import models.ReportRequestDTO;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author Laura Hansen s184234
 */
public class MerchantServant {
    private Merchant merchant;
    private UUID id;


    public MerchantServant(UUID id) {
        this.id = id;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID registerMerchant(Merchant merchant) throws Exception {
        RestCommunicator dtuPay = new RestCommunicator(RestCommunicator.Service.MERCHANT);
        Object object = dtuPay.post(merchant, "/merchant", 201);
        HashMap<String, String> customerObject = (HashMap<String, String>) object;
        this.id = UUID.fromString(customerObject.get("merchantID"));
        return id;
    }

    public UUID requestPayment(int amount, UUID merchantID, UUID tokenID) throws Exception {
        RestCommunicator communicator = new RestCommunicator(RestCommunicator.Service.MERCHANT);
        JsonObject payment = Json.createObjectBuilder()
                .add("amount", amount)
                .add("merchantID", merchantID.toString())
                .add("tokenID", tokenID.toString())
                .add("merchantAccountID", "Empty...")
                .build();
            Object responseEntity = communicator.post(payment,"/payment", 200);
            HashMap<String, String> paymentIdEntity = (HashMap<String, String>) responseEntity;
            return UUID.fromString(paymentIdEntity.get("paymentID"));
    }


    public List<Payment> requestReport(UUID merchantID) throws Exception {
        RestCommunicator dtuPay = new RestCommunicator(RestCommunicator.Service.CUSTOMER);
        ReportRequestDTO reportRequestDTO = new ReportRequestDTO();
        reportRequestDTO.setFromDate(new Date());
        reportRequestDTO.setToDate(new Date());
        reportRequestDTO.setMerchantID(merchantID);

        Object object = dtuPay.post(reportRequestDTO, "/report", 201);
        List<Payment> report = getPayments((HashMap<String,?>) object);
        return report;
    }

    private List<Payment> getPayments(HashMap<String,?> object) {
        return (List<Payment>) object.get("payments");
    }
}
