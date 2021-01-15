# @author: Benjamin Eriksen & Rubatharisan Thirumathyam
Feature: Check token validity

Scenario: Token is valid
  Given a tokenID and a customerID "1234"
  And that token is valid
  When validity is checked
  Then the response is the customerID "1234"

Scenario: Token doesn't exist
  Given a fake tokenID and a customerID "1234"
  When validity is checked
  Then an exception with message "Token doesn't exist" is thrown

