Feature: Testing SOAP Service connection
  Scenario: Create account
    Given a customer with name "Test" "Testsen" and CPR "123456-9012" with a balance of 123 kroners
    When the customer is created
    And the account is fetched
    Then the balance should be 123 kroners
