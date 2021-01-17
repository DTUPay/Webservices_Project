package dtupay;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;
import exceptions.PaymentException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.IdGenerator;
import models.Payment;
import org.junit.After;
import org.junit.Before;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author Mikkel Rosenfeldt Anderson & Laura
 */
public class PaymentSteps {
    /**
     * Implementation of cucumber tests for service internal tasks
     */
}
