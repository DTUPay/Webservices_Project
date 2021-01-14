package models;

import dtu.ws.fastmoney.User;

import java.util.List;

public class Customer {
    private int CustomerID;
    private String FirstName;
    private String LastName;
    private String CPRNumber;
    private List<Token> Tokens;

    public Customer(){

    }

    public User customerToUser() {
        User newUser = new User();
        newUser.setLastName(this.LastName);
        newUser.setFirstName(this.FirstName);
        newUser.setCprNumber(this.CPRNumber);
        return newUser;
    }

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getCPRNumber() {
        return CPRNumber;
    }

    public void setCPRNumber(String CPRNumber) {
        this.CPRNumber = CPRNumber;
    }

    public List<Token> getTokens() {
        return Tokens;
    }

    public void setTokens(List<Token> tokens) {
        Tokens = tokens;
    }
}
