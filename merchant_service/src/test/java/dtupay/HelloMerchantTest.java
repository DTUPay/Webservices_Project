package dtupay;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class HelloMerchantTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/merchant_service")
          .then()
             .statusCode(200)
             .body(is("Welcome to Merchant Service!"));
    }

}
