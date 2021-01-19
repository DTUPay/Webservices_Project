# Author: Rubatharisan Thirumathyam & Oliver O. Nielsen

Feature: Customer creates himself
  Scenario: A customer creates himself
    Given a customer with bank account, firstname and lastname
    When the customer requests a DTUPay account
    Then the customer posses a DTUPay account

  Scenario: A customer creates himself with no bank account
    Given the customer have no bank account
    When the customer requests a DTUPay account
    Then the customer will not be provided a DTUPay account

  Scenario: A customer creates himself with no firstname
    Given the customer have no firstname
    When the customer requests a DTUPay account
    Then the customer will not be provided a DTUPay account

  Scenario: A customer creates himself with no lastname
    Given the customer have no lastname
    When the customer requests a DTUPay account
    Then the customer will not be provided a DTUPay account
