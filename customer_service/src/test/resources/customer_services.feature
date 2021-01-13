Feature:
  Scenario: Add a new customer
    Given a new customer with name "Jens" "Jensen" and CPR "110996xxxx"
    When the new customer is added to the repository
    Then a customer with name "Jens" "Jensen" and CPR "110996xxxx" exists in the repository

  Scenario: Remove a customer
    Given a new customer with name "Jens" "Jensen" and CPR "110996xxxx"
    And the new customer is added to the repository
    When a customer with CPR "110996xxxx" is removed from the repository
    Then a customer with CPR "110996xxxx" does not exist in the repository
