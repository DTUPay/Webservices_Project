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
