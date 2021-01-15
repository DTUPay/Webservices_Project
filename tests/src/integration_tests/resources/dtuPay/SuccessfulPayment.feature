Feature: Successful Payment
  Background: All users are set up
    Given a customer with name "Jens" "Jensen" and cpr number "121012-xxxx" and balance 100 DKK has a bank account
    * the customer is registered with DTU Pay
    * a merchant with name "Mads" "Madsen" and cpr number "140467-xxxx" and balance 1000 DKK has a bank account
    * the merchant is registered with DTU Pay
    * the customer has 5 tokens
    * the merchant has requested a payment of 20 DKK

  Scenario: Successful payment
    Given the customer has 5 tokens
    And there is a payment to be paid by the customer
    When the customer authorizes the payment
    * the customer request to see his account balance
    * the merchant request to see his account balance
    Then the payment succeeds
    * the token is consumed
    * the customer has 80 DKK in his account
    * the merchant has 1020 DKK in his account

    #Accounts need to be deregistered and closed by the @After statement



