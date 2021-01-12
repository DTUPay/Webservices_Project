package dtupay;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class HelloPaymentTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/payment_service")
          .then()
             .statusCode(200)
             .body(is("Welcome to Payment Service!"));
    }

}
