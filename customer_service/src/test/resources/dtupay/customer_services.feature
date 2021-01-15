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
