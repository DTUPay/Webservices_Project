package models;
/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */

import dtupay.RabbitMq;

import javax.json.JsonObject;
import java.util.UUID;

public class Message {
    private String event;
    private UUID requestId;
    private UUID messageId;
    private String payload;
    private Callback callback;

    public Message(){
        this.messageId = UUID.randomUUID();
        this.callback = new Callback();
    }

    public Message(RabbitMq rabbitMq){
        this.messageId = UUID.randomUUID();
        this.callback = new Callback();
        this.callback.setService(rabbitMq.queue);

    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
