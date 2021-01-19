package dtupay;

import dto.CustomerDTO;
import dto.PaymentDTO;
import dto.RefundDTO;
import dto.TokenDTO;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.User;
import exceptions.BankException;
import exceptions.PaymentException;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.Merchant;
import models.Payment;
import models.PaymentStatus;
import models.Token;
import org.junit.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.UUID;

/**
 * @author Oliver O. Nielsen & Björn Wilting
 */
public class PaymentSteps {
    PaymentService service = PaymentService.getInstance();
    PaymentDTO payment;
    RefundDTO refund;
    TokenDTO refundToken;
    CustomerDTO customerDTO;
    Merchant merchant;
    TokenDTO tokenDTO;
    String customerAccountNumber;
    String someOtherAccountNumber;
    final String customerCpr = "110996xxxx";
    final String someOtherCpr = "110997xxxx";
    String merchantAccountNumber;
    final String merchantCpr = "12345678";
    UUID customerId = UUID.randomUUID();
    UUID merchantID = UUID.randomUUID();
    UUID paymentId;
    float amount = 10;
    BigDecimal customerInitialBalance;
    BigDecimal merchantInitialBalance;
    Exception exception;


    @Before
    public void constructMerchantAndCustomer() throws BankServiceException_Exception {
        //Free required accounts
        try{
            service.bankService.retireAccount(service.bankService.getAccountByCprNumber(customerCpr).getId());
        } catch (BankServiceException_Exception e){}
        try{
            service.bankService.retireAccount(service.bankService.getAccountByCprNumber(merchantCpr).getId());
        } catch (BankServiceException_Exception e) {}
        try{
            service.bankService.retireAccount(service.bankService.getAccountByCprNumber(someOtherCpr).getId());
        } catch (BankServiceException_Exception e) {}

        //Create merchant in bank
        User merchantUser = new User();
        merchantUser.setCprNumber(merchantCpr);
        merchantUser.setFirstName("Some");
        merchantUser.setLastName("Business");
        merchantAccountNumber = service.bankService.createAccountWithBalance(merchantUser, BigDecimal.valueOf(0));

        //Create user in bank
        User customerUser = new User();
        customerUser.setCprNumber(customerCpr);
        customerUser.setFirstName("Some");
        customerUser.setLastName("User");
        customerAccountNumber = service.bankService.createAccountWithBalance(customerUser, BigDecimal.valueOf(amount));

        //Create user in bank
        User someOtherUser = new User();
        someOtherUser.setCprNumber(someOtherCpr);
        someOtherUser.setFirstName("Some other");
        someOtherUser.setLastName("User");
        someOtherAccountNumber = service.bankService.createAccountWithBalance(someOtherUser, BigDecimal.valueOf(amount));

        //Get initial balance
        customerInitialBalance = service.bankService.getAccount(customerAccountNumber).getBalance();
        merchantInitialBalance = service.bankService.getAccount(merchantAccountNumber).getBalance();
    }


    @Given("a customer")
    public void a_customer() {
        //Create customer
        customerDTO = new CustomerDTO();
        customerDTO.setCustomerID(customerId);
        customerDTO.setAccountNumber(customerAccountNumber);
        customerDTO.setFirstName("Jens");
        customerDTO.setLastName("Jensen");
    }

    @Given("a token")
    public void a_token() {
        //Create token
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 3);
        Token token = new Token();
        token.setTokenID(UUID.randomUUID());
        token.setCustomerID(customerId);
        token.setExpiryDate(calendar.getTime());
        tokenDTO = new TokenDTO(token);
    }

    @Given("a merchant")
    public void a_merchant() {
        //Create merchant
        merchant = new Merchant("Sportsshop", merchantAccountNumber, merchantID);
    }

    @Given("a payment")
    public void a_payment() {
        payment = new PaymentDTO();
        payment.setMerchantAccountID(merchantAccountNumber);
        payment.setTokenID(tokenDTO.getTokenID());
        payment.setAmount(amount);
        payment.setMerchantID(merchant.getMerchantID());
    }

    @Given("a payment has been made")
    public void a_payment_has_been_made() throws BankException {
        paymentId = service.createPayment(payment, customerDTO, tokenDTO);
    }

    @Given("a refund")
    public void a_refund() {
        //Create token
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 3);
        Token token = new Token();
        token.setTokenID(UUID.randomUUID());
        token.setCustomerID(customerId);
        token.setExpiryDate(calendar.getTime());
        refundToken = new TokenDTO(token);

        refund = new RefundDTO();
        refund.setPaymentID(paymentId);
        refund.setTokenID(refundToken.getTokenID());
    }

    @Given("a non registered payment id")
    public void a_non_registered_payment_id() {
        paymentId = UUID.randomUUID();
    }

    @When("the service creates a payment")
    public void the_service_creates_a_payment() {
        try {
            paymentId = service.createPayment(payment, customerDTO, tokenDTO);
        } catch (BankException e) {
            exception = e;
        }
    }

    @When("the service refunds a payment")
    public void the_service_refunds_a_payment() {
        try {
            service.refundPayment(refund, refundToken);
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("the service fetches the payment")
    public void the_service_fetches_the_payment() {
        try {
            service.getPayment(paymentId);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the bank transaction is made")
    public void the_bank_transaction_is_made() throws BankServiceException_Exception {
        BigDecimal customerCurrentBalance = service.bankService.getAccount(customerAccountNumber).getBalance();
        BigDecimal merchantCurrentBalance = service.bankService.getAccount(merchantAccountNumber).getBalance();

        Assert.assertNull(exception);

        Assert.assertEquals(customerCurrentBalance, (customerInitialBalance.subtract(BigDecimal.valueOf(amount))));
        Assert.assertEquals(merchantCurrentBalance, (merchantInitialBalance.add(BigDecimal.valueOf(amount))));
    }

    @Then("a payment is registered in the repository")
    public void a_payment_is_registered_in_the_repository() throws PaymentException {
        Payment payment = service.getPayment(paymentId);
    }

    @Then("a refund is registered in the repository")
    public void a_refund_is_registered_in_the_repository() throws PaymentException {
        Payment refund = service.getPayment(paymentId);
        Assert.assertEquals(refund.getStatus(), PaymentStatus.REFUNDED);
    }

    @Then("the refund transaction is made")
    public void the_refund_transaction_is_made() throws BankServiceException_Exception {
        BigDecimal customerCurrentBalance = service.bankService.getAccount(customerAccountNumber).getBalance();
        BigDecimal merchantCurrentBalance = service.bankService.getAccount(merchantAccountNumber).getBalance();

        Assert.assertNull(exception);

        //Balances are back to initial
        Assert.assertEquals(customerCurrentBalance, customerInitialBalance);
        Assert.assertEquals(Double.valueOf(merchantCurrentBalance.toString()), Double.valueOf(merchantInitialBalance.toString()));
    }

    @Then("the second refund fails with error {string}")
    public void the_second_refund_fails_with_error(String string) {
        Assert.assertEquals(exception.getMessage(), string);
    }

    @Then("the balances of the customer and merchant accounts are equals the initial balance")
    public void the_balances_of_the_customer_and_merchant_accounts_are_equals_the_initial_balance() throws BankServiceException_Exception {
        BigDecimal customerCurrentBalance = service.bankService.getAccount(customerAccountNumber).getBalance();
        BigDecimal merchantCurrentBalance = service.bankService.getAccount(merchantAccountNumber).getBalance();

        //Balances are back to initial
        Assert.assertEquals(customerCurrentBalance, customerInitialBalance);
        Assert.assertEquals(Double.valueOf(merchantCurrentBalance.toString()), Double.valueOf(merchantInitialBalance.toString()));

    }

    @Then("the fetch operation fails")
    public void the_fetch_operation_fails() {
        Assert.assertNotNull(exception);
    }

    @Given("a payment which the customer cannot afford")
    public void a_payment_which_the_customer_cannot_afford() {
        payment = new PaymentDTO();
        payment.setMerchantAccountID(merchantAccountNumber);
        payment.setTokenID(tokenDTO.getTokenID());
        payment.setAmount(amount+1000);
        payment.setMerchantID(merchant.getMerchantID());
    }

    @Then("the bank transaction is not successful")
    public void the_bank_transaction_is_not_successful() throws BankServiceException_Exception {
        BigDecimal customerCurrentBalance = service.bankService.getAccount(customerAccountNumber).getBalance();
        BigDecimal merchantCurrentBalance = service.bankService.getAccount(merchantAccountNumber).getBalance();

        Assert.assertNotNull(exception);

        Assert.assertEquals(customerCurrentBalance, customerInitialBalance);
        Assert.assertEquals(merchantCurrentBalance, merchantInitialBalance);
    }

    @Then("a payment is not registered in the repository")
    public void a_payment_is_not_registered_in_the_repository() {
        Payment p = null;
        try {
             p = service.getPayment(paymentId);
        } catch (PaymentException e) {

        }
        Assert.assertNull(p);
    }

    @Given("the merchant spends his money")
    public void the_merchant_spends_his_money() throws BankServiceException_Exception {
        BigDecimal merchantCurrentBalance = service.bankService.getAccount(merchantAccountNumber).getBalance();
        service.bankService.transferMoneyFromTo(
                merchant.getAccountNumber(), // Money from
                someOtherAccountNumber, // Money to
                BigDecimal.valueOf(payment.getAmount()),
                "Some payment out of merchants account");
         merchantCurrentBalance = service.bankService.getAccount(merchantAccountNumber).getBalance();
    }

    @Then("a refund is not registered in the repository")
    public void a_refund_is_not_registered_in_the_repository() throws PaymentException {
        Payment refund = service.getPayment(paymentId);
        Assert.assertNotEquals(refund.getStatus(), PaymentStatus.REFUNDED);
    }

    @Then("the refund transaction is not made")
    public void the_refund_transaction_is_not_made() throws BankServiceException_Exception {
        BigDecimal customerCurrentBalance = service.bankService.getAccount(customerAccountNumber).getBalance();
        BigDecimal merchantCurrentBalance = service.bankService.getAccount(merchantAccountNumber).getBalance();

        BigDecimal expectedBalance= customerInitialBalance.subtract(BigDecimal.valueOf(payment.getAmount()));
        Assert.assertEquals(customerCurrentBalance, expectedBalance);
    }

    @After
    public void deconstructMerchantAndCustomer() throws BankServiceException_Exception {
        service.bankService.retireAccount(customerAccountNumber);
        service.bankService.retireAccount(merchantAccountNumber);
    }

}
