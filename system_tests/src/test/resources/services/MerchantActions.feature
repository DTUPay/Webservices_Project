Feature: Actions that can be performed by a merchant

  Scenario: A merchant requests a payment
    Given a merchant
    When the merchant requests a payment with an amount of 10 DKK
    Then the merchant is given the paymentId of the payment



