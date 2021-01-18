Feature: Partial scenarios for a successful payment

  #Partial scenarios
  Scenario: Create customer bank account
    Given a customer with name "Jens" "Jensen" and cpr number "121012-xxxx" and balance 100 DKK
    When the customer registers at the bank
    Then the customer is registered at the bank
  Scenario: Create merchant bank account
    Given a merchant with name "Mads" "Madsen" and cpr number "140467-xxxx" and balance 1000 DKK
    When the merchant register at the bank
    Then the merchant is registered at the bank
  Scenario: The customer registers in the DTU Pay system
    Given a customer that is registered at the bank
    When the customer registers at DTU Pay
    Then the customer is registered with DTU Pay
  Scenario: The merchant registers in the DTU Pay system
    Given a merchant that is registered at the bank
    When the merchant registers at DTU Pay
    Then the merchant is registered with DTU Pay
  Scenario: The customer requests new tokens
    Given a customer that is registered at DTU Pay
    And the customer has 0 tokens
    When the customer requests 5 new tokens
    Then the customer is given 5 new tokens
  #Conflict - Required in sequence diagram but not in pseudo code!
  Scenario: The merchant requests a payment
    Given a merchant that is registered at DTU Pay
    When the merchant requests a payment of 20 DKK
    Then the merchant is given the payment id of the requested payment
  Scenario: The customer accepts a payment
    Given a customer that is registered at DTU Pay
    And the customer has 5 tokens
    And there is a payment to be paid by the customer
    When the customer accepts the payment
    Then the payment succeeds
    And the token is consumed
  Scenario: The customer checks his account balance
    Given a customer that is registered at DTU Pay
    When the customer requests his account balance
    Then an account balance of 80 DKK is returned to the customer
  Scenario: The merchant checks his account balance
    Given a merchant that is registered at DTU Pay
    When the merchant requests his account balance
    Then an account balance of 1020 DKK is returned to the merchant
  Scenario: The customer closes his bank account
    Given a customer that is registered at DTU Pay
    When the customer requests to have his bank account closed
    Then the customer does not have an account at DTU Pay
    And the customer does not have an account at the bank
  Scenario: The merchant closes his bank account
    Given a merchant that is registered at DTU Pay
    When the merchant requests to have his bank account closed
    Then the merchant does not have an account at DTU Pay
    And the merchant does not have an account at the bank
