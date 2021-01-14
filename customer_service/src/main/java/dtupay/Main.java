package dtupay;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {
    public static CustomerService service;
    public static void main(String[] args) {
        service = new CustomerService();
        Quarkus.run();
    }
}
