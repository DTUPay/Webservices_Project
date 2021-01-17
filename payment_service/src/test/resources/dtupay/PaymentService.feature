Feature: Testing Payment Service standalone


  Scenario: Get payment
    Given a merchant with a token initializes a payment for 20 kroners
    When he request the payment in the app
    Then he receives a paymentID with the type UUID
    And the payment can be found using paymentID

  Scenario Outline: Negative or zero payment
    Given a merchant with ID <merchantID> who wants a payment for <amount> kroners
    When he request the payment in the app
    Then an exception is made with the message <arg0>
    Examples:
      | merchantID | amount | arg0                          |
      | 10         | -1     | "Non positive payment amount" |
      | 10         | 0      | "Non positive payment amount" |

  Scenario: Invalid merchant ID
    Given a merchant with ID -10 who wants a payment for 10 kroners
    When he request the payment in the app
    Then an exception is made with the message "Non positive merchant ID"

  Scenario Outline: Get manager summary
    Given there are <arg0> payments made
    When the manager requests a summary
    Then he gets a summary with <arg0> payments
    Examples:
      | arg0 |
      | 3    |
      | 10   |
      | 0    |

  Scenario: Get customer summary
    Given
