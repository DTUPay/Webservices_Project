package models;
/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */


import javax.json.JsonObject;
import java.util.UUID;

public class Message {
    private String event;
    private String service;
    private UUID requestId;
    private int status = 200;
    private String statusMessage;
    private UUID messageId = UUID.randomUUID();
    private Payload payload;
    private Callback callback = new Callback();

    public Message(){

    }

    public Message(String service, String event){
        this.setService(service);
        this.setEvent(event);
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

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
