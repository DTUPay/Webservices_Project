package servants;


import models.Customer;
import models.Merchant;
import models.Payment;
import models.ReportRequestDTO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ManagementServant {
    public UUID registerCustomer(Customer customer) throws Exception {
        RestCommunicator dtuPay = new RestCommunicator(RestCommunicator.Service.CUSTOMER);
        Object object = dtuPay.post(customer, "/customer", 201);
        HashMap<String, String> customerObject = (HashMap<String, String>) object;
        return UUID.fromString(customerObject.get("customerID"));
    }

    public UUID registerMerchant(Merchant merchant) throws Exception {
        RestCommunicator dtuPay = new RestCommunicator(RestCommunicator.Service.MERCHANT);
        Object object = dtuPay.post(merchant, "/merchant", 201);
        HashMap<String, String> customerObject = (HashMap<String, String>) object;
        return UUID.fromString(customerObject.get("merchantID"));
    }

    public List<Payment> requestReport() throws Exception {
        RestCommunicator dtuPay = new RestCommunicator(RestCommunicator.Service.CUSTOMER);
        ReportRequestDTO reportRequestDTO = new ReportRequestDTO();
        reportRequestDTO.setFromDate(new Date());
        reportRequestDTO.setToDate(new Date());

        Object object = dtuPay.post(reportRequestDTO, "/report", 201);
        List<Payment> report = getPayments((HashMap<String,?>) object);
        return report;
    }

    private List<Payment> getPayments(HashMap<String,?> object) {
        return (List<Payment>) object.get("payments");
    }
}
