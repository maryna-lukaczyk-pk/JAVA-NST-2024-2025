Feature: Assign user to project
  Scenario: Assign a user to a project
    Given a new user exists
    And a new project exists
    When I assign the user to the project
    Then the response status should be 200
    And the user should be assigned to the project
    And the project should be assigned to the user