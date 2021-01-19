Feature: Testing Payment Service standalone

#Authros: Oliver O. Nielsen & Björn Wilting
  Scenario: The service creates a payment
    Given a customer
    * a token
    * a merchant
    * a payment
    When the service creates a payment
    Then the bank transaction is made
    * a payment is registered in the repository

  Scenario: The service creates a payment he cannot afford
    Given a customer
    * a token
    * a merchant
    * a payment which the customer cannot afford
    When the service creates a payment
    Then the bank transaction is not successful
    * a payment is not registered in the repository


  Scenario: The service refunds a payment
    Given a customer
    * a token
    * a merchant
    * a payment
    * a payment has been made
    * a refund
    When the service refunds a payment
    Then a payment is registered in the repository
    * a refund is registered in the repository
    * the refund transaction is made

  Scenario: The service refunds a payment that the merchant cannot afford
    Given a customer
    * a token
    * a merchant
    * a payment
    * a payment has been made
    * the merchant spends his money
    * a refund
    When the service refunds a payment
    Then a payment is registered in the repository
    * a refund is not registered in the repository
    * the refund transaction is not made

  Scenario: The service refunds a already refunded payment
    Given a customer
    * a token
    * a merchant
    * a payment
    * a payment has been made
    * a refund
    When the service refunds a payment
    * the service refunds a payment
    Then a payment is registered in the repository
    * a refund is registered in the repository
    * the second refund fails with error "Payment already refunded"
    * the balances of the customer and merchant accounts are equals the initial balance

  Scenario: The service attempts to fetch a non registered payment
    Given a non registered payment id
    When the service fetches the payment
    Then the fetch operation fails
