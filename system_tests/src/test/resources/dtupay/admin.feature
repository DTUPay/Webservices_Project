Feature: Admin
  Scenario: admin creates new customer
    Given An admin user
    And a user of type "Customer" with information firstname "Bob", lastname "Bobsen" and cpr "12345678"
    When admin creates user
    Then the user of type "Customer" has been added to the system

  Scenario: admin creates new merchant
    Given An admin user
    And a user of type "Merchant" with information firstname "Bob", lastname "Bobsen" and cpr "12345678"
    When admin creates user
    Then the user of type "Merchant" has been added to the system


  Scenario: admin removes a customer
    Given An admin user
    And a user of type "Customer" with cpr "12345678"
    When admin removes the user
    Then the user has been removed from the system

  Scenario: admin removes a merchant
    Given An admin user
    And a user of type "Merchant" with cpr "12345678"
    When admin removes the user
    Then the user has been removed from the system

