/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */

package dtupay;

import models.Message;

import javax.json.JsonObject;

public interface IRabbitMq {
    void parseMessage(String queue);

    void sendMessage(String queue, Message message);
    void processMessage(JsonObject jsonObject);

}
