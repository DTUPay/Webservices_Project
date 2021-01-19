package dtuPay;

import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.Customer;
import models.Merchant;
import models.Payment;
import models.Report;
import servants.CustomerServant;
import servants.ManagementServant;
import servants.MerchantServant;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class ReportingSteps {
    private ManagementServant accountManagement;
    private CustomerServant customerServant;
    private MerchantServant merchantServant;
    UUID merchantID;
    UUID customerID;
    Report managerReport = null;
    Report merchantReport = null;
    Report customerReport = null;
    private Customer customer;
    private Merchant merchant;
    private Exception exception;
    private BankServiceService service;
    private BankService bank;
    private List<Payment> report;

    @Before
    public void CreateUsersInBank() throws Exception {
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


        customerID = accountManagement.registerCustomer(customer);
        merchantID = accountManagement.registerMerchant(merchant);
        customerServant = new CustomerServant(customerID);
        merchantServant = new MerchantServant(customerID);

    }


    @Given("{int} transactions have been made")
    public void transactionsHaveBeenMade(int arg0) throws Exception {
        //Generate merchant & customer transactions
        customerServant.requestTokens(customerID,4);
        for(int i = 0; i < arg0; i++){
            merchantServant.requestPayment(i+15,merchantID,customerServant.selectToken());
        }
    }

    @When("the customer requests a report")
    public void theCustomerRequestsAReport() throws Exception {
        report = customerServant.requestReport(customerID);
    }

    @When("the merchant requests a report")
    public void theMerchantRequestsAReport() {
        throw new PendingException();
        //report = merchantServant.requestReport(merchantID);
    }


    @When("the manager requests a manager report")
    public void theManagerRequestsAManagerReport() {
        throw new PendingException();
        //report = accountManagement.requestReport();
    }

    @Then("the report contains {int} payments")
    public void theReportContainsPayments(int numPayments) {
        assertEquals(report.size(),numPayments);
    }

    @After
    public void removeUserBankAccounts() throws BankServiceException_Exception {
        bank.retireAccount(customer.getAccountNumber());
        bank.retireAccount(merchant.getAccountNumber());
    }
}



