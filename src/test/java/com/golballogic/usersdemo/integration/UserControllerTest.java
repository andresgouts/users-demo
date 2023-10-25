package com.golballogic.usersdemo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.golballogic.usersdemo.UsersDemoApplication;
import com.golballogic.usersdemo.domain.User;
import com.golballogic.usersdemo.dto.PhoneDTO;
import com.golballogic.usersdemo.dto.request.CreateUserRequest;
import com.golballogic.usersdemo.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = UsersDemoApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void userCreationSuccess() throws Exception {

        PhoneDTO phoneDto = PhoneDTO.builder()
                .number(123L).build();

        CreateUserRequest userRequest = CreateUserRequest
                .builder()
                .email("test@test.com")
                .password("Password21")
                .phones(List.of(phoneDto))
                .build();

        mockMvc.perform(post("/api/v1/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated());

        User user = userRepository.findOneUserByEmail(userRequest.getEmail()).get();
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getPassword());
        assertNotNull(user.getLastLogin());
    }

    @Test
    public void userCreationBadEmail() throws Exception {

        CreateUserRequest userRequest = CreateUserRequest
                .builder()
                .email("test.com")
                .password("Password21")
                .build();

        mockMvc.perform(post("/api/v1/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void userCreationBadPassword() throws Exception {

        CreateUserRequest userRequest = CreateUserRequest
                .builder()
                .email("test@test.com")
                .password("Password")
                .build();

        mockMvc.perform(post("/api/v1/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void userCreationExistingUser() throws Exception {

        CreateUserRequest userRequest = CreateUserRequest
                .builder()
                .email("test@test.com")
                .password("Password21")
                .build();

        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        userRepository.save(user);


        mockMvc.perform(post("/api/v1/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());

    }
}
