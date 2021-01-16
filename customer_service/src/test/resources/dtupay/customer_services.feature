Feature: Manage customers
  Scenario: Add a new customer to the repository
    Given a new customer with name "Jens" "Jensen" and CPR "110996xxxx" that does not exist in the repository
    When the new customer is added to the repository
    Then a customer with name "Jens" "Jensen" and CPR "110996xxxx" exists in the repository

  Scenario: Add a new customer to the repository
    Given a new customer with name "Jan" "Jansen" and CPR "110995xxxx" that does exist in the repository
    When the new customer is added to the repository
    Then an error message with "Customer with CPR: 110995xxxx already exists" is thrown

  Scenario: Remove an existing customer
    Given a new customer with name "Jens" "OtherJensen" and CPR "120996xxxx" that does exist in the repository
    When a customer with CPR "120996xxxx" is removed from the repository
    Then a customer with CPR "120996xxxx" does not exist in the repository

  Scenario: Remove an non existing customer
    Given a new customer with name "Fake" "Newsen" and CPR "123456xxxx" that does not exist in the repository
    When a customer with CPR "123456xxxx" is removed from the repository
    Then an error message with "Customer with CPR: 123456xxxx doesn't exist" is thrown
    And a customer with CPR "123456xxxx" does not exist in the repository

  Scenario: Get unused token
    Given a new customer with name "Rich" "Kid" and CPR "310819xxxx" that does exist in the repository
    And has a unused token
    And the customer request a unused token
    Then the token is removed from the customers token

  Scenario: Get unused token - no more tokens left
    Given a new customer with name "Poor" "Kid" and CPR "111111xxxx" that does exist in the repository
    And the customer request a unused token
    Then an error message with "No more tokens left" is thrown

  Scenario: Get unused token - no more tokens left
    Given a new customer with name "What" "Kid" and CPR "111111????" that does not exist in the repository
    And the customer request a unused token
    Then an error message with "Customer with CPR: 111111???? doesn't exist" is thrown

  Scenario Outline: Requests tokens - fail
    Given a new customer with name <firstName> <lastName> and CPR <cpr> that does exist in the repository
    And has <arg0> unused token
    When he requests more tokens
    Then an error message with <msg> is thrown
    Examples:
      | firstName | lastName | cpr          | arg0 | msg                                              |
      | "Poor"    | "Kid"    | "111111xxxx" | 2    | "Customer with ID: 111111xxxx still has 2 left." |
      | "Poor"    | "Kid"    | "111111xxxx" | 3    | "Customer with ID: 111111xxxx still has 3 left." |
      | "Poor"    | "Kid"    | "111111xxxx" | 4    | "Customer with ID: 111111xxxx still has 4 left." |
      | "Poor"    | "Kid"    | "111111xxxx" | 5    | "Customer with ID: 111111xxxx still has 5 left." |

  Scenario Outline: Requests tokens - success
    Given a new customer with name <firstName> <lastName> and CPR <cpr> that does exist in the repository
    And has <arg0> unused token
    When he requests more tokens
    Then his request is successful
    Examples:
      | firstName | lastName | cpr          | arg0 |
      | "Poor"    | "Kid"    | "111111xxxx" | 0    |
      | "Poor"    | "Kid"    | "111111xxxx" | 1    |
