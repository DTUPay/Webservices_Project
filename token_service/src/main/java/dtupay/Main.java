package dtupay;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.enterprise.event.Observes;

@QuarkusMain
public class Main {
    public static TokenService tokenService;

    public static void main(String[] args) {
        System.out.println("Running main method");
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tokenService = new TokenService();
        System.out.println("Ran token service");
        Quarkus.run();
    }

    void onStart(@Observes StartupEvent ev) {
        System.out.println("Token service startup event fired!");
    }
}
