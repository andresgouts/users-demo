Feature: Users creation and retrieval
  Scenario: User is created successfully
    When the client calls user creation endpoint
    Then the client receives status code of 201
    And the client receives user created data