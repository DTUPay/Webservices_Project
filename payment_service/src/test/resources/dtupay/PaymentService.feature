Feature: Testing Payment Service standalone

  Scenario: Get payment
    Given a merchant with a bank account
    Given a token to authorize a payment by a customer
    Given a payment of 20 DDK to be paid
    When the merchant requests the payment in the app
    Then he receives a paymentID of type UUID
    And the payment can be found using the paymentID

  Scenario Outline: Negative or zero payment
    Given a merchant with a bank account
    Given a token to authorize a payment by a customer
    Given a payment of <amount> to be paid
    When the merchant requests the payment in the app
    Then an exception is made with the message <arg0>
    Examples:
      | merchantID | amount | arg0                          |
      | 10         | -1     | "Non positive payment amount" |
      | 10         | 0      | "Non positive payment amount" |

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
