/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */
package dtupay;

import javax.json.JsonObject;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface IRabbitMq {
    public void parseMessage(String queue);
    public void sendMessage(String queue, JsonObject message);

}
