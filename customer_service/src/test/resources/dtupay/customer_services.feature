Feature: Manage customers
  Scenario: Add a new customer to the repository
    Given a new customer with name "Jens" "Jensen" and customerID "110996xxxx" that does not exist in the repository
    When the new customer is added to the repository
    Then a customer with name "Jens" "Jensen" and customerID "110996xxxx" exists in the repository

  Scenario: Add a new customer to the repository
    Given a new customer with name "Jan" "Jansen" and customerID "110995xxxx" that does exist in the repository
    When the new customer is added to the repository
    Then an error message with "Customer with customerID: 110995xxxx already exists" is thrown

  Scenario: Remove an existing customer
    Given a new customer with name "Jens" "OtherJensen" and customerID "120996xxxx" that does exist in the repository
    When a customer with customerID "120996xxxx" is removed from the repository
    Then a customer with customerID "120996xxxx" does not exist in the repository

  Scenario: Remove an non existing customer
    Given a new customer with name "Fake" "Newsen" and customerID "123456xxxx" that does not exist in the repository
    When a customer with customerID "123456xxxx" is removed from the repository
    Then an error message with "Customer with customerID: 123456xxxx doesn't exist" is thrown
    And a customer with customerID "123456xxxx" does not exist in the repository

  Scenario: Get unused token
    Given a new customer with name "Rich" "Kid" and customerID "310819xxxx" that does exist in the repository
    And has a unused token
    And the customer request a unused token
    Then the token is removed from the customers token

  Scenario: Get unused token - no more tokens left
    Given a new customer with name "Poor" "Kid" and customerID "111111xxxx" that does exist in the repository
    And the customer request a unused token
    Then an error message with "No more tokens left" is thrown

  Scenario: Get unused token - no more tokens left
    Given a new customer with name "What" "Kid" and customerID "111111????" that does not exist in the repository
    And the customer request a unused token
    Then an error message with "Customer with customerID: 111111???? doesn't exist" is thrown
