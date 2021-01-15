/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */

package dtupay;


import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;


@Path("/reports")
public class ReportingAPI {
    ReportingService service = new ReportingService();

    // DEBUG METHOD
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/merchant/{merchantId}/")
    public void getReportByMerchant(@PathParam("merchantId") String merchantId, @Suspended AsyncResponse response) throws Exception {
        service.getReportByMerchant(merchantId, response);
    }


}
