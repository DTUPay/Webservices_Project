Feature: Check token validity

  Scenario: A new token is created
    Given a request for 1 tokens for a customer with id 1234
    Then those tokens are created for the customer

  Scenario: 5 new token are created
    Given a request for 5 tokens for a customer with id 1234
    Then those tokens are created for the customer

