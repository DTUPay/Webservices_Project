Feature: Requesting refunds
  Background: All users are set up
    Given a customer has a bank account provided by the bank with a balance of 100 DKK
    * the customer is registered with DTU Pay
    * a merchant has a bank account provided by the bank with a balance of 1000 DKK
    * the merchant is registered with DTU Pay
    * the customer has 3 tokens
    * a successful payment of 50 DKK has been made to the merchant by the customer

  Scenario: Successful refund
    Given the customer has the paymentID of his last payment
    When the customer request to have the payment refunded
    * the merchant request to see his account balance
    * the customer request to see his account balance
    Then the payment is refunded
    * the customer has 100 DKK in his account
    * the merchant has 1000 DKK in his account

  Scenario: Attempting to refund an already refunded payment
    Given the customer already has refunded a given payment
    * the customer has 100 DKK in his account
    * the merchant has 1000 DKK in his account
    When the customer request to have the payment refunded
    Then the refunding fails
    * the error message is "400 Error while refunding payment: Payment already refunded"
    * the customer has 100 DKK in his account
    * the merchant has 1000 DKK in his account


  Scenario: Attempting to refund with an invalid token
    Given the customer has the paymentID of his last payment
    When the request to have the payment refunded using an invalid token
    * the merchant request to see his account balance
    * the customer request to see his account balance
    Then the refunding fails
    Then the error message is "400 The token could not be validated"
    * the customer has 50 DKK in his account
    * the merchant has 1050 DKK in his account
