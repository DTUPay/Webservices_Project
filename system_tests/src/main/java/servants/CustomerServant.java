package servants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import servants.RestCommunicator.Service;

import javax.ws.rs.core.GenericType;

/**
 * @author Björn Wilting s184214
 */
public class CustomerServant {
    private String id;
    private List<Token> tokens;

    public CustomerServant(String id) {
        this.id = id;
        this.tokens = new ArrayList<>();
    }

    public void acceptPayment(String paymentID, Token token) {
    }

    public void requestTokens(Integer requestedTokens) throws Exception {
        RestCommunicator communicator = new RestCommunicator(Service.CUSTOMER.port);
        String path = Service.CUSTOMER.port + "/Tokens";
            Object responseEntity = communicator.post(requestedTokens,path);

    }

    public void requestRefund(String paymentID) {

    }
}
