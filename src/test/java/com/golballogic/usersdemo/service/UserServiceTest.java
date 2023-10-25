package com.golballogic.usersdemo.service;

import com.golballogic.usersdemo.domain.User;
import com.golballogic.usersdemo.dto.PhoneDTO;
import com.golballogic.usersdemo.dto.UserDto;
import com.golballogic.usersdemo.dto.request.CreateUserRequest;
import com.golballogic.usersdemo.dto.response.UserCreationResponse;
import com.golballogic.usersdemo.exception.UserCreationException;
import com.golballogic.usersdemo.repository.PhoneRepository;
import com.golballogic.usersdemo.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PhoneRepository phoneRepository;
    @Spy
    ModelMapper modelMapper;
    @Spy
    PasswordEncoder encoder;
    @InjectMocks
    UserService userService;


    @Test
    public void saveUserSuccessTest() {
        PhoneDTO phoneDto = PhoneDTO.builder()
                .number(123L).build();

        CreateUserRequest userRequest = CreateUserRequest
                .builder()
                .email("test@test.com")
                .password("Password21")
                .phones(List.of(phoneDto))
                .build();
        User user = modelMapper.map(userRequest, User.class);
        when(userRepository.save(any())).thenReturn(user);
        when(userRepository.findOneUserByEmail(userRequest.getEmail())).thenReturn(Optional.empty());

        UserCreationResponse response = userService.saveUser(userRequest);
        assertEquals(response.getEmail(), userRequest.getEmail());
        assertNotNull(response.getToken());
    }

    @Test(expected = UserCreationException.class)
    public void saveUserBadEmailTest() {
        PhoneDTO phoneDto = PhoneDTO.builder()
                .number(123L).build();

        CreateUserRequest userRequest = CreateUserRequest
                .builder()
                .email("test.com")
                .password("Password21")
                .phones(List.of(phoneDto))
                .build();

        userService.saveUser(userRequest);
    }

    @Test(expected = UserCreationException.class)
    public void saveUserBadPasswordTest() {
        PhoneDTO phoneDto = PhoneDTO.builder()
                .number(123L).build();

        CreateUserRequest userRequest = CreateUserRequest
                .builder()
                .email("test@test.com")
                .password("password2")
                .phones(List.of(phoneDto))
                .build();

        userService.saveUser(userRequest);
    }

    @Test(expected = UserCreationException.class)
    public void saveUserAlreadyExists() {
        PhoneDTO phoneDto = PhoneDTO.builder()
                .number(123L).build();

        CreateUserRequest userRequest = CreateUserRequest
                .builder()
                .email("test@test.com")
                .password("Password21")
                .phones(List.of(phoneDto))
                .build();
        User user = modelMapper.map(userRequest, User.class);
        when(userRepository.findOneUserByEmail(userRequest.getEmail())).thenReturn(Optional.of(user));

        userService.saveUser(userRequest);

    }

    @Test
    public void loginFindsUser() {
        PhoneDTO phoneDto = PhoneDTO.builder()
                .number(123L).build();

        UserDto userDto = UserDto
                .builder()
                .email("test@test.com")
                .password("Password21")
                .phones(List.of(phoneDto))
                .created(Date.from(Instant.now()))
                .name("name")
                .lastLogin(Date.from(Instant.now()))
                .build();
        User user = modelMapper.map(userDto, User.class);

        when(userRepository.save(any())).thenReturn(user);
        when(userRepository.findOneUserByEmail(userDto.getEmail())).thenReturn(Optional.of(user));

        UserDto response = userService.login(userDto.getEmail());

        assertEquals(response.getEmail(), userDto.getEmail());
        assertNotNull(response.getToken());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loginDoesNotFindsUser() {
        PhoneDTO phoneDto = PhoneDTO.builder()
                .number(123L).build();

        UserDto userDto = UserDto
                .builder()
                .email("test@test.com")
                .password("Password21")
                .phones(List.of(phoneDto))
                .created(Date.from(Instant.now()))
                .name("name")
                .lastLogin(Date.from(Instant.now()))
                .build();
        User user = modelMapper.map(userDto, User.class);

        when(userRepository.findOneUserByEmail(userDto.getEmail())).thenReturn(Optional.empty());

        userService.login(userDto.getEmail());
    }

}