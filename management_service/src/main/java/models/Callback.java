/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam
 */
package models;

public class Callback {
    private String service;
    private String event;

    public Callback(){

    }
    public Callback(String service, String event){
        setService(service);
        setEvent(event);
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
