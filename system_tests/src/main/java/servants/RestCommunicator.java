package servants;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;

/**
 * @author Björn Wilting s184214
 */
public class RestCommunicator {
    private Client client;
    private WebTarget target;

    public RestCommunicator(String port) {
        this.client = ClientBuilder.newClient();
        this.target = client.target("http://localhost:"+port+"/");
    }

    public <T> Object post(T item, String location) throws Exception {
        Response response = target.path(location).request().post(Entity.entity(item, MediaType.APPLICATION_JSON));
        if(response.getStatus() == 201)
            return response.readEntity(Object.class);
        else throw new Exception(response.getStatus() + " Error!");

    }

    public enum Service {
        CUSTOMER ("8080", "customer_service"),
        MANAGEMENT ("8081", "management_service"),
        MERCHANT ("8082", "merchant_service");

        protected final String port;
        protected final String location;
        Service(String port, String location) {
            this.port = port;
            this.location = location;
        }
    }
}
