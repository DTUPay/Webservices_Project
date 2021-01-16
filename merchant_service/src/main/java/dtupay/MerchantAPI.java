package dtupay;


import dto.PaymentDTO;
import dto.ReportRequestDTO;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import java.util.UUID;


@Path("/merchant_service")
public class MerchantAPI {
    MerchantService service = new MerchantService();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Welcome to Merchant Service!";
    }

    @POST
    @Path("/payment")
    @Consumes(MediaType.APPLICATION_JSON)
    //Should return boolean
    public void requestPayment(@Suspended AsyncResponse response, PaymentDTO paymentDTO) {
        service.broker.requestPayment(paymentDTO, response);
    }

    @POST
    @Path("/report")
    @Consumes(MediaType.APPLICATION_JSON)
    //Should return boolean
    public void generateReport(@Suspended AsyncResponse response, ReportRequestDTO reportRequestDTO) {
        service.broker.generateReport(reportRequestDTO, response);
    }

}
