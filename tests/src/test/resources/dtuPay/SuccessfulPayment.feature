Feature: Successful Payment
  Background: All users are set up
    Given a customer has a bank account provided by the bank
    * the customer is registered with DTU Pay
    * a merchant has a bank account provided by the bank
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



