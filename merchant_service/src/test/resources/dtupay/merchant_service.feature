Feature: Manage merchants

  Scenario: Add a new merchant
    Given a merchant with name "Trading A/S" and accountNumber "110996xxxx" that does not exist in the repository
    When the new merchant is added to the repository
    Then a merchant with name "Trading A/S" and accountNumber "110996xxxx" exists in the repository

  Scenario: Remove an existing customer
    Given a merchant with name "Trading Inc" and accountNumber "120994xxxx" that does exist in the repository
    When the merchant is removed from the repository
    Then the merchant does not exist in the repository

  Scenario: Remove an non existing customer
    Given a merchant with name "Scam Corp." and accountNumber "123456xxxx" that does not exist in the repository
    When the merchant is removed from the repository
    Then an error message with "Merchant with given merchantID doesn't exist" is thrown
    And the merchant does not exist in the repository
