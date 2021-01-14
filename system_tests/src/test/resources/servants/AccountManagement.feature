#Author: Björn Wilting s184214
  # & Laura Sønderskov Hansen s...
Feature: Account Management

  Scenario: Adding a new Customer
    Given a user with the name "Bob" "Bobsen" and the cpr "020967-1234"
    When the user registers for the app
    Then a customer with the name "Bob" "Bobsen" and the cpr "020967-1234" has been created

  Scenario: Removing a Customer
    Given a customer with id "123456"
    When the customer requests to be deleted
    Then the customer with id "123456" is deleted from the system
    And there is no customer with id "123456" registered in the system

  Scenario: Adding a merchant
    Given a company with the name "Trading A/S" and cvr "12345678"
    When the company registers for the app
    Then a merchant with the name "Trading A/S" and cvr "12345678" has been created

  Scenario: Removing a Merchant
    Given a merchant with id "54321"
    When the merchant requests to be deleted
    Then the merchant with id "54321" is deleted from the system
    And there is no merchant with id "54321" registered in the system

  Scenario: User request report
  #To be defined
