Feature: Making a payment
  Background: All users are set up
    Given a customer has a bank account provided by the bank with a balance of 100 DKK
    * the customer is registered with DTU Pay
    * a merchant has a bank account provided by the bank with a balance of 1000 DKK
    * the merchant is registered with DTU Pay

  Scenario: Successful payment
    Given the customer has 5 tokens
    When the customer selects a token
    * the merchant authorizes a payment with the customers token and an amount of 20 DKK
    * the customer request to see his account balance
    * the merchant request to see his account balance
    Then the payment succeeds
    * the token is consumed
    * the customer has 80 DKK in his account
    * the merchant has 1020 DKK in his account


  Scenario: Attempt to pay with invalid token
    Given the customer has no tokens
    When the customer provides an invalid token
    * the merchant authorizes a payment with the customers token and an amount of 20 DKK
    * the customer request to see his account balance
    * the merchant request to see his account balance
    Then the payment fails
    * the error message is "404 The token could not be validated!"
    * the customer has 100 DKK in his account
    * the merchant has 1000 DKK in his account

  Scenario: Payment with insufficient funds
    Given the customer has 1 tokens
    When the customer selects a token
    * the merchant authorizes a payment with the customers token and an amount of 120 DKK
    * the customer request to see his account balance
    * the merchant request to see his account balance
    Then the payment fails
    * the error message is "400 Error while making payment: Debtor balance will be negative"
    * the customer has 100 DKK in his account
    * the merchant has 1000 DKK in his account

