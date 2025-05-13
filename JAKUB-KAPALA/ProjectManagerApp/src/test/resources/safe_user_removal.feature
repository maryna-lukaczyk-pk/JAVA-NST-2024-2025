Feature: Safe user removal
  Scenario: User can be safely removed via User API
    Given a user exists
    And a project exists
    And user is assigned to a project
    When I delete the user
    Then user is deleted
    And project does not preserve user assignment