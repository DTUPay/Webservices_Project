
Feature: Generating reports based on previous transactions

  Scenario Outline: Generate manager report
    Given <arg0> transactions have been made
    When a <arg01> requests a report
    Then a manager report is received with <arg02> transactions
    Examples:
      | arg0 | arg01     | arg02 |
      | 100  | "manager" | 200   |
      | 10  | "manager" | 20   |
      | 0  | "manager" | 0   |

  Scenario Outline: Generate merchant report
    Given <arg0> transactions have been made
    When a <arg01> requests a report
    Then a merchant report is received with <arg02> transactions
    Examples:
      | arg0 | arg01      | arg02 |
      | 100  | "merchant" | 10    |
      | 5  | "merchant" | 5    |
      | 0  | "merchant" | 0    |

  Scenario Outline: Generate customer report
    Given <arg0> transactions have been made
    When a <arg01> requests a report
    Then a customer report is received with <arg02> transactions
    Examples:
      | arg0 | arg01      | arg02 |
      | 100  | "customer" | 20    |
      | 0  | "customer" | 0    |
