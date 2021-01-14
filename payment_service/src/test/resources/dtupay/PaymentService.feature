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

  Scenario: Unsuccessful get payment
    Given a merchant with ID 10 who wants a payment for 20 kroners
    When he request the payment in the app
    Then he receives a paymentID with the type UUID
    And the payment cannot be found using the wrong paymentID

  Scenario: Get manager summary
    Given there are 3 payments made
    When the manager requests a summary
    Then he gets a summary with 3 payments
