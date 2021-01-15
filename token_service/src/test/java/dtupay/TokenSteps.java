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
    String tokenBelongsTo;
    private String errorMsg;

    @Given("a request for {int} tokens for a customer with id {string}")
    public void aRequestForTokensForACustomerWithId(int amount, String id) {
        this.customerID = id;
        this.tokenIDs = this.tokenService.addTokens(id, amount);
    }

    @Then("those tokens are created for the customer")
    public void thoseTokensAreCreatedForTheCustomer() throws TokenException {
        for (UUID tokenID : this.tokenIDs) {
            assertEquals(this.tokenService.isTokenValid(tokenID),this.customerID);
        }
    }


    @Given("a tokenID and a customerID {string}")
    public void aTokenIDAndACustomerID(String customerID) {
        this.customerID = customerID;
        this.tokenID = this.tokenService.addTokens(customerID, 1).get(0);

    }

    @And("a corresponding token in the token repository")
    public void aCorrespondingTokenInTheTokenRepository() throws TokenException {
        assertEquals(this.tokenService.isTokenValid(this.tokenID),this.customerID);
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
    public void theTokenIsNoLongerActive() {
        try {
            this.tokenService.isTokenValid(tokenID);
            fail();
        } catch (TokenException e) {
            assertEquals(e.getMessage(), "Token has already been used");
        }


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
            tokenBelongsTo = this.tokenService.isTokenValid(this.tokenID);
        } catch (TokenException te) {
            this.errorMsg = te.getMessage();
        }
    }

    @And("that token is valid")
    public void thatTokenIsValid() throws TokenException {
        this.tokenService.isTokenValid(this.tokenID);
    }

    @Then("the response is the customerID {string}")
    public void theResponseIsTheCustomerID(String customerID) throws TokenException {
        assertEquals(customerID,this.tokenService.isTokenValid(this.tokenID));
    }
}

