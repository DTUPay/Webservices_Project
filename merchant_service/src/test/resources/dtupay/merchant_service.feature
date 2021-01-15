Feature: Manage merchants

  Scenario: Add a new merchant
    Given a merchant with name "Trading A/S" and CVR "110996xxxx" that does not exist in the repository
    When the new merchant is added to the repository
    Then a merchant with name "Trading A/S" and CVR "110996xxxx" exists in the repository

  Scenario: Add a merchant that already exists in the repository
    Given a merchant with name "Trading Corp" and CVR "110998xxxx" that does exist in the repository
    When the new merchant is added to the repository
    Then an error message with "Merchant with CVR: 110998xxxx already exists" is thrown

  Scenario: Remove an existing customer
    Given a merchant with name "Trading Inc" and CVR "120994xxxx" that does exist in the repository
    When a merchant with CVR "120994xxxx" is removed from the repository
    Then a merchant with CVR "120994xxxx" does not exist in the repository

  Scenario: Remove an non existing customer
    Given a new merchant with name "Scam Corp." and CVR "123456xxxx"
    When a merchant with CVR "123456xxxx" is removed from the repository
    Then an error message with "Merchant with CVR: 123456xxxx doesn't exist" is thrown
    And a merchant with CVR "123456xxxx" does not exist in the repository
