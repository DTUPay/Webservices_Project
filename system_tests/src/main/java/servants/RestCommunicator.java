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

    /**
     *
     * @param item the object to be posted by the REST-communicator
     * @param path the path to the location where the post-call should occur
     * @param <T> the type of item to be posted
     * @return the response Entity (as an Object type) or a boolean inicating success
     * @throws Exception with the error code returned by the post-call in case of failure
     */
    public <T> Object post(T item, String path) throws Exception {
        Response response = target.path(path).request().post(Entity.entity(item, MediaType.APPLICATION_JSON));
        if(response.getStatus() == 201) {
            if(response.hasEntity())
                return response.readEntity(Object.class);
            else return true;
        }
        else throw new Exception(response.getStatus() + " Error!");

    }

    /**
     *
     * @param item the object to be put by the REST-communicator
     * @param path the path to the location where the item should be put
     * @param <T> the type of the object to be put
     * @return a boolean indication success
     * @throws Exception with the error code returned by the put-call in case of failure
     */
    public <T> boolean put(T item, String path) throws Exception {
        Response response = target.path(path).request().put(Entity.entity(item, MediaType.APPLICATION_JSON));
        if(response.getStatus() == 200)
            return true;
        else throw new Exception(response.getStatus() + " Error!");
    }

    /**
     * Service enum containing the information required to connect to the REST endpoints
     */
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
