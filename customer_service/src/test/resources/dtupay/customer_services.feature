Feature: Manage customers
  Scenario: Add a new customer to the repository
    Given a new customer with name "Jens" "Jensen" and accountID "110034" that does not exist in the repository
    When the new customer is added to the repository
    Then the customer exists in the repository

  Scenario: Remove an existing customer
    Given a new customer with name "Jens" "OtherJensen" and accountID "110025" that does exist in the repository
    When a customer with accountID "110025" is removed from the repository
    Then a customer with accountID "110025" does not exist in the repository

  Scenario: Remove an non existing customer
    Given a new customer with name "Fake" "Newsen" and accountID "123456xxxx" that does not exist in the repository
    When a customer with accountID "123456xxxx" is removed from the repository
    Then an error message with "Customer with given customerID doesn't exist" is thrown
    And a customer with accountID "123456xxxx" does not exist in the repository

  Scenario: Invalid customerID should throw a exception
    Given a random customerID that does not exist
    Then an error message with "Customer with given customerID doesn't exist" is thrown

  Scenario Outline: Requests tokens - fail
    Given a new customer with name <firstName> <lastName> and accountID <accountID> that does exist in the repository
    And has <arg0> unused token
    When he requests more tokens
    Then an error message with <msg> is thrown
    Examples:
      | firstName | lastName | accountID          | arg0 | msg                                              |
      | "Poor"    | "Kid"    | "111111xxxx" | 2    | "Customer with given customerID still has 2 left." |
      | "Poor"    | "Kid"    | "111111xxxx" | 3    | "Customer with given customerID still has 3 left." |
      | "Poor"    | "Kid"    | "111111xxxx" | 4    | "Customer with given customerID still has 4 left." |
      | "Poor"    | "Kid"    | "111111xxxx" | 5    | "Customer with given customerID still has 5 left." |

  Scenario Outline: Requests tokens - success
    Given a new customer with name <firstName> <lastName> and accountID <accountID> that does exist in the repository
    And has <arg0> unused token
    When he requests more tokens
    Then his request is successful
    Examples:
      | firstName | lastName | accountID          | arg0 |
      | "Poor"    | "Kid"    | "111111xxxx" | 0    |
      | "Poor"    | "Kid"    | "111111xxxx" | 1    |

  Scenario Outline: Requests tokens - fail
    Given a new customer with name <firstName> <lastName> and accountID <accountID> that does not exist in the repository
    When he requests more tokens
    Then an error message with <msg> is thrown
    Examples:
      | firstName | lastName | accountID           | msg                       |
      | "Poor"    | "Kid"    | "111111xxxx"  | "Customer does not exist" |

  Scenario Outline: Add tokens - success
    Given a new customer with name <firstName> <lastName> and accountID <accountID> that does exist in the repository
    And has <arg0> unused token
    When he requests more tokens
    Then his request is successful
    When he gets <arg1> more tokens
    Then he has a total of <arg2> tokens
    Examples:
      | firstName | lastName | accountID          | arg0 | arg1 | arg2 |
      | "Rich"    | "Kid"    | "111111xxxx" | 0    | 3    | 3    |
      | "Rich"    | "Kid"    | "111111xxxx" | 1    | 4    | 5    |

  Scenario Outline: Add tokens - fail
    Given a new customer with name <firstName> <lastName> and accountID <accountID> that does not exist in the repository
    When he gets <arg1> more tokens
    Then an error message with <msg> is thrown
    Examples:
      | firstName | lastName | accountID           | arg1 | msg |
      | "Rich"    | "Kid"    | "111111xxxx"  | 3    | "Customer with given customerID doesn't exist"    |
      | "Rich"    | "Kid"    | "111111xxxx"  | 4    | "Customer with given customerID doesn't exist"    |
