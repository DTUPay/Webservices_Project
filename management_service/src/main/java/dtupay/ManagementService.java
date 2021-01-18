package dtupay;
// @author: Rubatharisan Thirumathyam

import brokers.ManagementBroker;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class ManagementService {
    ManagementBroker broker;
    private static ManagementService instance = new ManagementService();

    public ManagementService() {
        broker = new ManagementBroker(this);
    }

    public static ManagementService getInstance(){
        return instance;
    }

    public static void main(String[] args) {
        ManagementService service = ManagementService.getInstance();
        Quarkus.run();
    }


}
