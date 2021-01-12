Feature: Account Management

  Scenario: Adding a new Customer
    Given a user with the name "Bob" "Bobsen" and the cpr "020967-1234"
    When the user registers for the app
    Then a customer with the name "Bob" "Bobsen" and the cpr "020967-1234" has been created

  Scenario: Adding a merchant
    Given a company with the name "Trading A/S" and cvr "12345678"
    When the company registers for the app
    Then a merchant with the name "Trading A/S" and cvr "12345678" has been created

#Add remove scenarios?
