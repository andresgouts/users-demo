package com.golballogic.usersdemo.cucumber.users;

import com.golballogic.usersdemo.cucumber.CucumberApplicationTest;
import com.golballogic.usersdemo.dto.request.CreateUserRequest;
import com.golballogic.usersdemo.dto.response.UserCreationResponse;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class UsersStepDefinitions extends CucumberApplicationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<UserCreationResponse> lastUserCreationResponse;
    private final String EMAIL = "test@test.com";
    private final String USER_NAME = "test";

    @When("the client calls user creation endpoint")
    public void createUserCall() {
        CreateUserRequest requestBody = CreateUserRequest.builder().email(EMAIL).
                password("Gor1llA2").
                name(USER_NAME)
                .build();

        lastUserCreationResponse = restTemplate.postForEntity("/api/v1/users",
                requestBody,
                UserCreationResponse.class);
    }

    @Then("^the client receives status code of (\\d+)$")
    public void clientResponseStatusValidation(int status) {
        HttpStatus currentStatus = lastUserCreationResponse.getStatusCode();
        assertEquals(status, currentStatus.value());
    }

    @Then("the client receives user created data")
    public void clientReceivesData() {
        UserCreationResponse response = lastUserCreationResponse.getBody();
        assertEquals(EMAIL, response.getEmail());
        assertEquals(USER_NAME, response.getName());
    }

}
