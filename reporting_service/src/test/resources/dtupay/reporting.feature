Feature: Generating reports based on previous transactions

  Scenario: Generate manager report

    Given a manager requesting a report
    Then a manager report is received

  Scenario: Generate merchant report

    Given a merchant requesting a report
    Then a merchant report is received

  Scenario: Generate customer report

    Given a customer requesting a report
    Then a customer report is received
