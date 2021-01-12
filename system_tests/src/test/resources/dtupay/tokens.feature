Feature: Token
  Scenario: a customer request tokens
    Given a user of type "Customer" with information firstname "Bob", lastname "Bobsen" and cpr "12345678"
    And the user has 0 tokens
    When the user requests 5 tokens
    Then the user has 5 tokens

