# Benjamin & ..

Feature: Requesting new token
  Background: All users are set up
    Given a customer has a bank account provided by the bank with a balance of 100 DKK
    * the customer is registered with DTU Pay
    * a merchant has a bank account provided by the bank with a balance of 1000 DKK
    * the merchant is registered with DTU Pay

  Scenario: A customer requests new tokens
    Given the customer has 0 tokens
    When the customer requests 4 new tokens
    Then the customer possesses 4 tokens

  Scenario: A customer requests too many tokens
    Given the customer has 1 tokens
    When the customer requests 8 new tokens
    Then the customer possesses 1 tokens
    And the error message is "400 Customer token request would exceed more than 6 tokens"

  Scenario: A customer requests tokens while possessing more than 1
    Given the customer has 2 tokens
    When the customer requests 3 new tokens
    Then the customer possesses 2 tokens
    And the error message is "400 Customer with given customerID still has 2 left."
