Feature: customer_service unit tests
  Scenario: Get a customer successfully
    Given a customer with cpr "110996xxxx" exists
    When looking for a customer with cpr "110996xxxx"
    Then a unique customer with cpr "110996xxxx" is found

  Scenario: Get a customer error
    Given a customer with cpr "110996xxxx" does not exist
    When looking for a customer with cpr "110996xxxx"
    Then no customer is found
