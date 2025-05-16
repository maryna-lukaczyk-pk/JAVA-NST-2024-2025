Feature: Assign user to project

  Scenario: Assign a new user to a new project
    Given a new user exists
    And a new project exists
    When I assign the user to the project
    Then the response status should be 200
    And the user should be assigned to the project
