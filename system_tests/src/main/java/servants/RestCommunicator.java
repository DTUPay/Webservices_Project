package servants;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

    public <T> String post(T item, String location) {
        Response response = target.path(location).request().post(Entity.entity(item, MediaType.APPLICATION_JSON));
        return response.getHeaderString("location");
    }

    public enum Service {
        CUSTOMER ("8080"),
        MANAGEMENT ("8081"),
        MERCHANT ("8082");

        protected final String port;
        Service(String port) {
            this.port = port;
        }
    }
}
