package dtupay;


import dto.PaymentDTO;
import dto.ReportRequestDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;


@Path("/merchant_service")
public class MerchantAPI {
    MerchantService service = new MerchantService();

    @POST
    @Path("/payment")
    @Consumes(MediaType.APPLICATION_JSON)
    //Should return boolean
    public void requestPayment(@Suspended AsyncResponse response, PaymentDTO paymentDTO) {
        System.out.println("merchant api account number: " + paymentDTO.getMerchantAccountID() + " end of number");
        service.broker.requestPayment(paymentDTO, response);
    }

    @POST
    @Path("/report")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //Should return boolean
    public void generateReport(@Suspended AsyncResponse response, ReportRequestDTO reportRequestDTO) {
        service.broker.generateReport(reportRequestDTO, response);
    }

}
