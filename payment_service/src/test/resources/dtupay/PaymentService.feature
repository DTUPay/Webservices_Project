Feature: Testing Payment Service standalone
  Scenario: Request payment
    Given a merchant with ID 10 who wants a payment for 20 kroners
    When he request the payment in the app
    Then he receives a paymentID with the type UUID

  Scenario: Get payment
    Given a merchant with ID 10 who wants a payment for 20 kroners
    When he request the payment in the app
    Then he receives a paymentID with the type UUID
    And the payment can be found using paymentID

  Scenario: Negative payment
    Given a merchant with ID 10 who wants a payment for -1 kroners
    When he request the payment in the app
    Then an exception is made with the message "Non positive payment amount"

  Scenario: Invalid merchant ID
    Given a merchant with ID -10 who wants a payment for 10 kroners
    When he request the payment in the app
    Then an exception is made with the message "Non positive merchant ID"

  Scenario: Get manager summary
    Given there are 3 payments made
    When the manager requests a summary
    Then he gets a summary with 3 payments
