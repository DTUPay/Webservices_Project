# @author: Benjamin Eriksen & Rubatharisan Thirumathyam
Feature: Use Token

  Scenario: A token is used
    Given a tokenID and a customerID 1234
    And a corresponding token in the token repository
    When the token is used
    Then the token is no longer active

  Scenario: The token has already been used
    Given a tokenID and a customerID 1234
    And a corresponding token in the token repository
    And that token has been used
    When the token is used
    Then an exception with message "Token has already been used" is thrown

  Scenario: Token doesn't exist
    Given a fake tokenID and a customerID 1234
    When the token is used
    Then an exception with message "Token doesn't exist" is thrown


