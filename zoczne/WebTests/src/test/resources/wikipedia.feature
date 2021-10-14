Feature: Testing Wikipedia webpage

  Scenario: Simple test
    Given Go to Wikipedia Page
    When Do nothing
    Then Nothing happens

  Scenario: Search for info about 'Lodz'
    When I search for 'Lodz'
    Then I obtain a sigle result Page

  Scenario: Search for 'Independence'
    When I search for 'Independence'
    Then I see a list of proposed wiki pages
    And In the results ther is a record 'gmina w stanie Iowa, w hrabstwie Appanoose'
    And This result have working page