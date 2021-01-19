package dtupay;
// @author: Oliver O. Nielsen
import models.Message;

import javax.ws.rs.container.AsyncResponse;
import java.util.HashMap;
import java.util.UUID;

public class MessageRepository {
    public HashMap<UUID, Message> pendingMessages = new HashMap<>();
    private static MessageRepository instance = new MessageRepository();

    private MessageRepository(){}

    //Get the only object available
    public static MessageRepository getInstance(){
        return instance;
    }

    public boolean containsMessageObject(UUID requestId){
        return this.pendingMessages.containsKey(requestId);
    }

    public Message getMessageObject(UUID requestId){
        return this.pendingMessages.get(requestId);
    }

    public void removeMessageObject(UUID requestId){
        pendingMessages.remove(requestId);
    }

    public UUID saveMessageObject(Message message){
        UUID uuid = message.getRequestId();
        pendingMessages.put(uuid, message);
        return uuid;
    }

    public Integer getSizeOfMessages() {
        return this.pendingMessages.size();
    }
}
