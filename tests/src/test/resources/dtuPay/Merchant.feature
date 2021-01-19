# Author: Rubatharisan Thirumathyam & Oliver O. Nielsen

Feature: Merchant creates himself
  Scenario: A merchant creates himself
    Given a merchant with bank account and name
    When the merchant requests a DTUPay account
    Then the merchant posses a DTUPay account

  Scenario: A merchant creates himself with no bank account
    Given the merchant have no bank account
    When the merchant requests a DTUPay account
    Then the merchant will not be provided a DTUPay account

  Scenario: A merchant creates himself with no name
    Given the merchant have no name
    When the merchant requests a DTUPay account
    Then the merchant will not be provided a DTUPay account
