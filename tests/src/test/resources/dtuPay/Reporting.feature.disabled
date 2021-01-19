Feature: Generating reports
  Background:
    Given 4 transactions have been made

  Scenario: A customer requests a customer report
    When the customer requests a report
    Then the report contains 4 payments

  Scenario: A merchant requests a merchant report
    When the merchant requests a report
    Then the report contains 4 payments

  Scenario: A manager requests a manager report
    When the manager requests a manager report
    Then the report contains 4 payments
