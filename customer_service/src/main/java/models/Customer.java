package models;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int customerID;
    private String firstName;
    private String lastName;
    private String CPRNumber;
    private List<Token> tokens;

    public Customer(){

    }

    public Customer(int id, String firstName, String lastName, String CPRNumber){
        this.customerID = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.CPRNumber = CPRNumber;
        //Initialize empty array if none is provided
        this.tokens = new ArrayList<>();
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCPRNumber() {
        return CPRNumber;
    }

    public void setCPRNumber(String CPRNumber) {
        this.CPRNumber = CPRNumber;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }
}
