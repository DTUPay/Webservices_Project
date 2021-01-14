/*
@author Benjamin Eriksen & Rubatharisan Thirumathyam
 */

package dtupay;

import exceptions.TokenException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TokenSteps {

    TokenService tokenService = new TokenService();
    ArrayList<UUID> tokenIDs;
    UUID tokenID;
    String customerID;
    boolean isValid;
    private String errorMsg;

    @Given("a request for {int} tokens for a customer with id {string}")
    public void aRequestForTokensForACustomerWithId(int amount, String id) {
        this.customerID = id;
        this.tokenIDs = this.tokenService.addTokens(id, amount);
    }

    @Then("those tokens are created for the customer")
    public void thoseTokensAreCreatedForTheCustomer() throws TokenException {
        for (UUID tokenID : this.tokenIDs) {
            this.tokenService.isTokenValid(tokenID, this.customerID);
        }
    }


    @Given("a tokenID and a customerID {string}")
    public void aTokenIDAndACustomerID(String customerID) {
        this.customerID = customerID;
        this.tokenID = this.tokenService.addTokens(customerID, 1).get(0);

    }

    @And("a corresponding token in the token repository")
    public void aCorrespondingTokenInTheTokenRepository() throws TokenException {
        this.tokenService.isTokenValid(this.tokenID, this.customerID);
    }

    @When("the token is used")
    public void theTokenIsUsed() {
        try {
            this.tokenService.useToken(this.tokenID);
        } catch (TokenException te) {
            this.errorMsg = te.getMessage();
        }
    }

    @Then("the token is no longer active")
    public void theTokenIsNoLongerActive() throws TokenException {
        this.tokenService.isTokenValid(tokenID, this.customerID);

    }

    @And("that token has been used")
    public void thatTokenHasBeenUsed() {
        try {
            this.tokenService.useToken(this.tokenID);
        } catch (TokenException te) {
            this.errorMsg = te.getMessage();
        }
    }

    @Then("an exception with message {string} is thrown")
    public void anExceptionWithMessageIsThrown(String msg) {
        assertEquals(this.errorMsg, msg);


    }

    @Given("a fake tokenID and a customerID {string}")
    public void aFakeTokenIDAndACustomerID(String id) {
        this.tokenID = UUID.randomUUID();
    }

    @When("validity is checked")
    public void validityIsChecked() {
        try {
            this.isValid = this.tokenService.isTokenValid(this.tokenID,this.customerID);
        } catch (TokenException te) {
            this.errorMsg = te.getMessage();
        }
    }

    @Then("the response is valid")
    public void theResponseIsValid() {
        assertTrue(this.isValid);
    }

    @Then("the response is invalid")
    public void theResponseIsInvalid() {
        assertFalse(this.isValid);
    }

    @And("that token is valid")
    public void thatTokenIsValid() throws TokenException {
        assertTrue(this.tokenService.isTokenValid(this.tokenID,this.customerID));
    }

    @And("that token is invalid")
    public void thatTokenIsInvalid() throws TokenException {
        this.tokenService.useToken(tokenID);
        assertFalse(this.tokenService.isTokenValid(this.tokenID,this.customerID));
    }
}

