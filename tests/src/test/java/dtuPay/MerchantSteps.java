package dtuPay;

/*
@authors: Oliver O. Nielsen & Rubatharisan Thirumathyam
 */

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.MerchantDTO;
import org.junit.Assert;
import servants.*;

import java.util.HashMap;
import java.util.UUID;

public class MerchantSteps {
    private MerchantDTO merchant = new MerchantDTO();
    private UUID merchantID;
    private Exception exception;


    @Before
    public void resetMerchantId() {
        this.merchantID = null;
    }

    @Given("a merchant with bank account and name")
    public void a_merchant_with_bank_account_and_name() {
        merchant.setAccountNumber("my-bank-account-number");
        merchant.setName("Some Merchant Name");
    }

    @When("the merchant requests a DTUPay account")
    public void the_merchant_requests_a_dtu_pay_account() {
        try {
            RestCommunicator dtuPay = new RestCommunicator(RestCommunicator.Service.MERCHANT);
            Object object = dtuPay.post(merchant, "/merchant", 201);
            HashMap<String, String> customerObject = (HashMap<String, String>) object;
            this.merchantID = UUID.fromString(customerObject.get("merchantID"));
        } catch (Exception e) {
            System.out.print(e.toString());
            exception = e;
        }
    }

    @Then("the merchant posses a DTUPay account")
    public void the_merchant_posses_a_dtu_pay_account() {
        Assert.assertNotNull(this.merchantID);
        Assert.assertNull(exception);
    }

    @Given("the merchant have no bank account")
    public void the_merchant_have_no_bank_account() {
        merchant.setName("Some merchant name");
        merchant.setAccountNumber(null);
    }

    @Then("the merchant will not be provided a DTUPay account")
    public void the_merchant_will_not_be_provided_a_dtu_pay_account() {
        Assert.assertNull(this.merchantID);
        Assert.assertNotNull(exception);
    }

    @Given("the merchant have no name")
    public void the_merchant_have_no_name() {
        merchant.setName(null);
        merchant.setAccountNumber("my-bank-account-number");
    }

}
