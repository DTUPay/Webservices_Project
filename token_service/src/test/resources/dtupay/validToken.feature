# @author: Benjamin Eriksen & Rubatharisan Thirumathyam
Feature: Check token validity

Scenario: Token is valid
  Given a tokenID and a customerID "477a6e22-a499-4017-b307-704f16d76537"
  And that token is valid
  When validity is checked
  Then the response is the customerID "477a6e22-a499-4017-b307-704f16d76537"

Scenario: Token doesn't exist
  Given a fake tokenID and a customerID "477a6e22-a499-4017-b307-704f16d76537"
  When validity is checked
  Then an exception with message "Token doesn't exist" is thrown

