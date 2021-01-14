/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */

package dtupay;

import javax.json.JsonObject;

public interface IRabbitMq {
    void parseMessage(String queue);

    void sendMessage(String queue, JsonObject message, String callbackFucktion);
    void processMessage(JsonObject jsonObject);

}
