
Feature: Generating reports based on previous transactions

  Scenario Outline: Generate manager report
    Given <t0> transactions have been made
    When a <user> requests a report
    Then a manager report is received with <t1> transactions
    Examples:
      | t0 | user     | t1 |
      | 100  | "manager" | 200   |
      | 10  | "manager" | 20   |
      | 0  | "manager" | 0   |

  Scenario Outline: Generate merchant report
    Given <t0> transactions have been made
    When a <user> requests a report
    Then a merchant report is received with <t1> transactions
    Examples:
      | t0 | user      | t1 |
      | 100  | "merchant" | 100    |
      | 5  | "merchant" | 5    |
      | 0  | "merchant" | 0    |

  Scenario Outline: Generate customer report
    Given <t0> transactions have been made
    When a <user> requests a report
    Then a customer report is received with <t1> transactions
    Examples:
      | t0 | user      | t1 |
      | 100  | "customer" | 100    |
      | 0  | "customer" | 0    |
