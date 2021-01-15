Feature: Payment
  Scenario: Successful payment
    Given a customer with name "Test" "Testesen" cpr "123456-1234" and balance 123
    And a merchant with name "Trading" "A/S" cvr "123456-1234" and balance 123
    And the customer is registered
    And the merchant is registered
    And the customer gets 5 tokens


  customerAccount = bank.createAccount Name, CPR, balance
  merchantAccount = bank.createAccount Name, CPR, balance
  cid = customerAPI.registerCustomer Name, customerAccount, ...
  this will call dtuPay.registerCustomer ...
  mid = merchantAPI.registerMerchant Name, merchantAccount, ...
  this will call dtuPay.registerMerchant ...
  tokens = customerAPI.getTokensForCustomer 5, cid
  this will call dtuPay.getTokensForCustomer ...
  token = select a token from tokens
  merchantAPI.pay token, mid, amount
  this will call dtuPay.pay ....
  and in DTUPay, this will cause a call bank.moneyTransfer ...
  customerBalance = bank.getBalance customerAccount
  merchantBalance = bank.getBalance merchantAccount
  bank.retire customerAccount
  bank.retire merchantAccount
