/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */

package brokers;

import com.rabbitmq.client.DeliverCallback;
import models.Message;

import javax.json.JsonObject;

public interface IMessageBroker {
    void sendMessage(Message message) throws Exception;
    void onQueue(String queue, DeliverCallback callback);
    Message createReply(Message originalMessage);

}
