/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */

package dtupay;

import javax.json.JsonObject;

public interface IRabbitMq {
    public void parseMessage(String queue);
    public void sendMessage(String queue, JsonObject message);

}
