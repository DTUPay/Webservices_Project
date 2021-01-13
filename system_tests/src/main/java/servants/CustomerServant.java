package servants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import servants.RestCommunicator.Service;

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

    //Consider return type boolean?
    public void requestTokens(Integer requestedTokens) {
        RestCommunicator communicator = new RestCommunicator(Service.CUSTOMER.port);
        String path = Service.CUSTOMER.port + "/Tokens";
        String url = communicator.post(requestedTokens,path);

    }

    //Consider return type boolean?
    public void requestRefund(String paymentID) {

    }
}
