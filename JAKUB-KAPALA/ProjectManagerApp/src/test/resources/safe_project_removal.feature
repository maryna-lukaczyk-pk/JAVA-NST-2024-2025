Feature: Safe project removal
  Scenario: Project can be safely removed via Project API
    Given a project exists
      And a user exists
      And a task exists for the project
      And user is assigned to a project
    When I delete the project
    Then project is deleted
      And user does not preserve project assignment
      And task is deleted