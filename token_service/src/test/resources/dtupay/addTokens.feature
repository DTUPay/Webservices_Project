# @author: Benjamin Eriksen & Rubatharisan Thirumathyam
Feature: Add Tokens

  Scenario: A new token is created
    Given a request for 1 tokens for a customer with id "477a6e22-a499-4017-b307-704f16d76537"
    Then those tokens are created for the customer

  Scenario: 5 new token are created
    Given a request for 5 tokens for a customer with id "477a6e22-a499-4017-b307-704f16d76537"
    Then those tokens are created for the customer

