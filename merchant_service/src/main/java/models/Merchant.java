package models;

public class Merchant {
    private String name;
    private String cvr;

    public Merchant(String name, String cvr){
        this.cvr = cvr;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCVR() {
        return cvr;
    }

}
