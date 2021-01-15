/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen
 */

package dtupay;

import exceptions.CustomerException;
import io.quarkus.runtime.annotations.QuarkusMain;
import models.Customer;

import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.UUID;

public class CustomerService {
    public HashMap<UUID, AsyncResponse> pendingRequests = new HashMap<>();
    RabbitMq rabbitMq;
    ICustomerRepository customerRepository = new CustomerRepository();

    public CustomerService() {
        try {
            String serviceName = System.getenv("SERVICE_NAME"); //customer_service
            System.out.println(serviceName + " started");
            this.rabbitMq = new RabbitMq(serviceName, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UUID addPendingRequest(AsyncResponse asyncResponse) {
        UUID uuid = UUID.randomUUID();
        pendingRequests.put(uuid, asyncResponse);
        return uuid;
    }

    public void respondPendingRequest(UUID uuid, String data) {
        //TODO add data here
        System.out.println("Reply request with uuid: " + uuid.toString());
        System.out.println("Pending requests: " + pendingRequests.size());
        pendingRequests.get(uuid).resume(
                Response.status(200)
                        .entity(data)
                        .build()
        );
        pendingRequests.remove(uuid);
    }

    public void demo(JsonObject jsonObject) {
        // UUID needs to be trimmed after convertion from JSON
        String uuidString = jsonObject.get("uuid").toString().replaceAll("\"", "").replaceAll("\\\\", "");
        UUID uuid = UUID.fromString(uuidString);
        //respondPendingRequest(uuid, null);
    }

    public void requestTokens(JsonObject jsonObject) {
        JsonObject payload = (JsonObject) jsonObject.get("payload");

        //Get data from response
        String tokenIds = payload.get("tokenIds").toString().replaceAll("\"", "");

        System.out.println("Token ids gotten: " + tokenIds);
        // UUID needs to be trimmed after convertion from JSON
        String uuidString = jsonObject.get("requestId").toString()
                .replaceAll("\"", "")
                .replaceAll("\\\\", "");

        UUID uuid = UUID.fromString(uuidString);
        respondPendingRequest(uuid, tokenIds);

    }

    public void registerCustomer(Customer customer) throws CustomerException {
        if (!customerRepository.hasCustomer(customer.getCPRNumber())) {
            customerRepository.addCustomer(customer);
        }
        throw new CustomerException("Customer with CPR: " + customer.getCPRNumber() + " already exists");
    }

    public void removeCustomer(String cpr) throws CustomerException {

        if (customerRepository.hasCustomer(cpr)) {
            customerRepository.removeCustomer(cpr);
        }
        throw new CustomerException("Customer with CPR: " + cpr + " doesn't exist");
    }

    public Customer getCustomer(String cpr) throws CustomerException {
        if (customerRepository.hasCustomer(cpr)) {
            return customerRepository.getCustomer(cpr);
        }
        throw new CustomerException("Customer with CPR: " + cpr + " doesn't exist");
    }

    public boolean hasCustomer(String cprNumber) {
        return customerRepository.hasCustomer(cprNumber);
    }

}
