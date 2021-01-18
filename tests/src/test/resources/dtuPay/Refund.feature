Feature: Requesting refunds
  Background: All users are set up
    Given a customer has a bank account provided by the bank with a balance of 100 DKK
    * the customer is registered with DTU Pay
    * a merchant has a bank account provided by the bank with a balance of 1000 DKK
    * the merchant is registered with DTU Pay
    * the customer has been given 3 tokens
    * a successful payment of 50 DKK has been made to the merchant by the customer

  Scenario: Successful refund
    Given the customer has the paymentID of his last payment

