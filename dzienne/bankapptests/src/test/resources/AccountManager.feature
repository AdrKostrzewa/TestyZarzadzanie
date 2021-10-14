Feature: AccountManager class unit tests

Scenario: Internal transfer wrong source Account
  Given We have user with name "Tomasz"
  Given "Tomasz" has account number: 1 with amount: 1000.0
  Given We have account number: 2 with amount: 100.0
  Given All database operations are sucessfull
  Given All "Tomasz" operations are allowed
  When "Tomasz" make internal payment from acc: 3 to acc: 2 amount 100.0
  # There should be better exception thrown
  # Null pointer exception is not a good choice
  Then Expected Exception "java.lang.NullPointerException"

Scenario Outline: Proper Internal money flows
    Given We have user with name "<user>"
    Given "<user>" has account number: <srcAcc> with amount: <srcAmount>
    Given We have account number: <dstAcc> with amount: <dstAmount>
    Given All database operations are sucessfull
    Given All "<user>" operations are allowed
    When "<user>" make internal payment from acc: <srcAcc> to acc: <dstAcc> amount <amount>
    Then Account <srcAcc> have <expSrc>
    Then Account <dstAcc> have <expDst>
    Then Operation is successful
    Then Expect updates only on proper accounts
      |<srcAcc>|<dstAcc>|
    Then Withdraw was logged properly
    Then Payment was logged properly

Examples:
|user   |srcAcc |srcAmount  |dstAcc |dstAmount  |amount |expSrc |expDst |
|Tomasz |1      |200        |2      |100        |158.2  |41.8   |258.2  |
|Tomasz |1      |200        |3      |100        |200    |0      |300    |
|Alicja |3      |1000       |1      |0          |100    |900    |100    |

Scenario Outline: Operate with wrong ammount
  Given We have user with name "<user>"
  Given "<user>" has account number: <srcAcc> with amount: <srcAmount>
  Given We have account number: <dstAcc> with amount: <dstAmount>
  Given All database operations are sucessfull
  Given All "<user>" operations are allowed
  When "<user>" make internal payment from acc: <srcAcc> to acc: <dstAcc> amount <amount>
  Then Account <srcAcc> have <srcAmount>
  Then Account <dstAcc> have <dstAmount>
  Then Operation is unsuccessful
  Then No updates on accounts
  Then Withdraw was logged properly
  Then Payment was logged properly

Examples:
    |user   |srcAcc |srcAmount  |dstAcc |dstAmount  |amount|
    |Tomasz |1      |200        |2      |100        |-158.2|
    |Tomasz |1      |200        |3      |100        |201   |
    |Tomasz |1      |200        |3      |100        |0     |
    |Tomasz |1      |200        |3      |100        |0.0   |

Scenario Outline: User is not authorize to withdraw
    Given We have user with name "<user1>"
    Given "<user2>" has account number: <srcAcc> with amount: <srcAmount>
    Given We have account number: <dstAcc> with amount: <dstAmount>
    Given All database operations are sucessfull
    Given Users can operate on own accounts only
    When "<user1>" make internal payment from acc: <srcAcc> to acc: <dstAcc> amount <amount>
    Then Account <srcAcc> have <srcAmount>
    Then Account <dstAcc> have <dstAmount>
    Then Operation is unsuccessful
    Then No updates on accounts
    Then Operation was not authorized
    Then Unauthorized operation was logged properly


    Examples:
      |user1  |srcAcc |srcAmount  |dstAcc |dstAmount  |amount|user2 |
      |Tomasz |1      |200        |2      |100        |158.2|Maria |

Scenario Outline: User is not authorize to withdraw from destination account
    Given "<user1>" has account number: <srcAcc> with amount: <srcAmount>
    Given "<user2>" has account number: <dstAcc> with amount: <datAmount>
    Given All database operations are sucessfull
    Given Users can operate on own accounts only
    When "<user1>" make internal payment from acc: <srcAcc> to acc: <dstAcc> amount <amount>
    Then Account <srcAcc> have <srcAmount>
    Then Account <dstAcc> have <dstAmount>
    Then Operation is successful
    Then Expect updates only on proper accounts
    |<srcAcc>|<dstAcc>|
    Then Withdraw was logged properly
    Then Payment was logged properly


    Examples:
      |user1  |srcAcc |srcAmount  |dstAcc |dstAmount  |amount |user2 |expSrc  |expDst |
      |Tomasz |1      |200        |2      |100        |101.2  |Maria |98.8    |201.2  |

  Scenario: Database malfunction
    Given "<user1>" has account number: <srcAcc> with amount: <srcAmount>
    Given "<user2>" has account number: <dstAcc> with amount: <datAmount>
    Given All database operations are unsucessfull
    Given Users can operate on own accounts only
    When "<user1>" make internal payment from acc: <srcAcc> to acc: <dstAcc> amount <amount>
    #DB falied so no sense to ask for account states
    #Then Account <srcAcc> have <srcAmount>
    #Then Account <dstAcc> have <dstAmount>
    Then Operation is unsuccessful
    Then Expect updates only on proper accounts
      |<srcAcc>|<dstAcc>|
    Then Error was logged properly
    Then "EXCEPTION" ??