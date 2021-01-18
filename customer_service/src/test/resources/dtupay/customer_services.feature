Feature: Manage customers
  Scenario: Add a new customer to the repository
    Given a new customer with name "Jens" "Jensen" and accountID "110034" that does not exist in the repository
    When the new customer is added to the repository
    Then the customer exists in the repository

  Scenario: Remove an existing customer
    Given a new customer with name "Jens" "OtherJensen" and accountID "110025" that does exist in the repository
    When a customer with accountID "110025" is removed from the repository
    Then a customer with accountID "110025" does not exist in the repository

  Scenario: Remove an non existing customer
    Given a new customer with name "Fake" "Newsen" and accountID "123456xxxx" that does not exist in the repository
    When a customer with accountID "123456xxxx" is removed from the repository
    Then an error message with "Customer with given customerID doesn't exist" is thrown
    And a customer with accountID "123456xxxx" does not exist in the repository

  Scenario: Invalid customerID should throw a exception
    Given a random customerID that does not exist
    Then an error message with "Customer with given customerID doesn't exist" is thrown

  # Make more tests to get complete coverage of canRequestTokens and addTokens

