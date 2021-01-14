#Author: Björn Wilting s184214
  # & Laura Sønderskov Hansen s184234
Feature: Actions that can be performed by a customer

  Scenario: Accept a payment with token
    #Specify what a token is
    Given a customer with 1 token(s)
    And a pending payment with the payment id "somePaymentID" exists
    #Specify id
    When the customer accepts the payment with id "somePaymentID"
    Then the token is consumed
    And the payment succeeds

  Scenario: Attempt payment without token
    Given a customer with 0 token(s)
    And a pending payment with the payment id "somePaymentID" does not exists
    When the customer accepts the payment with id "somePaymentID"
    #Specify
    Then the payment fails

  Scenario: A customer without tokens requests tokens
    Given a customer with 0 token(s)
    When the customer requests 5 new tokens
    Then the customer has been given 5 tokens

  Scenario: A customer with a few tokens requests new tokens
    Given a customer with 2 token(s)
    When the customer requests 5 new tokens
    Then the token request is denied

  Scenario: A customer with all tokens requests new tokens
    Given a customer with 5 token(s)
    When the customer requests 2 new tokens
    Then the token request is denied
    And the customer has 5 tokens


