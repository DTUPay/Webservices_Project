package dtuPay;

import dtu.ws.fastmoney.*;
import dtupay.ReportingService;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.Customer;
import models.Merchant;
import models.Report;
import models.Token;
import servants.CustomerServant;
import servants.ManagementServant;
import servants.MerchantServant;
import testModels.TestPayment;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

public class ReportingSteps {
    ReportingService reportingService = ReportingService.getInstance();;
    private ManagementServant accountManagement;
    UUID merchantID = UUID.randomUUID();
    UUID customerID = UUID.randomUUID();
    Report managerReport = null;
    Report merchantReport = null;
    Report customerReport = null;
    private Customer customer;
    private Merchant merchant;
    private Exception exception;
    private BankServiceService service;
    private BankService bank;

    @Before
    public void CreateUsersInBank() throws BankServiceException_Exception {
        service = new dtu.ws.fastmoney.BankServiceService();
        bank = service.getBankServicePort();

        //Customer: Jensine Jensen, 121015-xxxx, 100 DKK
        customer = new Customer("Jensine", "Jensen", "121015-xxxx", 100);
        dtu.ws.fastmoney.User bankCustomer = new dtu.ws.fastmoney.User();
        bankCustomer.setFirstName("Jensine");
        bankCustomer.setLastName("Jensen");
        bankCustomer.setCprNumber("121515-0001");
        try {
            dtu.ws.fastmoney.Account customerAccount = bank.getAccountByCprNumber(bankCustomer.getCprNumber());
            bank.retireAccount(customerAccount.getId());
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        System.out.println("Creating customer bank account...");
        String customerBankAccount = bank.createAccountWithBalance(bankCustomer, new BigDecimal(100));
        System.out.println("CustomerBankAccount: " + customerBankAccount);
        customer.setAccountNumber(customerBankAccount);
        System.out.println("done");
        //Merchant: Mads Madsen, 140467-xxxx, 1000 DKK
        merchant = new Merchant("Madsine Madsen", "140468", 1000);
        User bankMerchant = new User();
        bankMerchant.setFirstName("Madsine");
        bankMerchant.setLastName("Madsen");
        bankMerchant.setCprNumber("140568-0001");


        try {
            Account merchantAccount = bank.getAccountByCprNumber(bankMerchant.getCprNumber());
            bank.retireAccount(merchantAccount.getId());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println("Creating merchant bank account...");
        String merchantBankAccount = bank.createAccountWithBalance(bankMerchant, new BigDecimal(1000));
        System.out.println("MerchantBankAccount: " + merchantBankAccount);
        merchant.setAccountNumber(merchantBankAccount);
        System.out.println("done");

        //TODO: Store account numbers
        accountManagement = new ManagementServant();

    }


    @Given("{int} transactions have been made")
    public void transactionsHaveBeenMade(int arg0) {
        //Generate merchant & customer transactions
        for(int i = 0; i < arg0; i++){
            //Merchant transaction
            TestPayment payment = new TestPayment(merchantID, new Random().nextInt(10000)+1);
            Token token = new Token(UUID.randomUUID());
            payment.setTokenID(token.getTokenID());
            payment.setCustomerID(token.getCustomerID());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, -i);
            payment.setDate(calendar.getTime());
            //if(new Random().nextInt(10) == 9)
            //    payment.setStatus(PaymentStatus.REFUNDED);
            reportingService.paymentRepository.addPayment(payment);

            //Customer transaction
            payment = new TestPayment(UUID.randomUUID(), new Random().nextInt(10000)+1);
            token = new Token(customerID);
            payment.setTokenID(token.getTokenID());
            payment.setCustomerID(customerID);
            payment.setDate(calendar.getTime());
            //if(new Random().nextInt(10) == 9)
            //    payment.setStatus(PaymentStatus.REFUNDED);
            reportingService.paymentRepository.addPayment(payment);
        }
    }

    @When("the customer requests a report")
    public void theCustomerRequestsAReport() {
    }

    @Then("the report contains {int} payments")
    public void theReportContainsPayments(int numPayments) {
    }

    @Given("the merchant has received {int} payments")
    public void theMerchantHasReceivedPayments(int numPayments) {
    }

    @When("the merchant requests a report")
    public void theMerchantRequestsAReport() {
    }

    @Given("that {int} payments have been made in the app")
    public void thatPaymentsHaveBeenMadeInTheApp(int numPayments) {
    }

    @When("the manager requests a manager report")
    public void theManagerRequestsAManagerReport() {
    }

    @After
    public void removeUserBankAccounts() throws BankServiceException_Exception {
        bank.retireAccount(customer.getAccountNumber());
        bank.retireAccount(merchant.getAccountNumber());
    }
}



