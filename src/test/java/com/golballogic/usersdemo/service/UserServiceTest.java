package com.golballogic.usersdemo.service;

import com.golballogic.usersdemo.domain.User;
import com.golballogic.usersdemo.dto.PhoneDTO;
import com.golballogic.usersdemo.dto.UserDto;
import com.golballogic.usersdemo.dto.request.CreateUserRequest;
import com.golballogic.usersdemo.dto.response.UserCreationResponse;
import com.golballogic.usersdemo.exception.AuthenticationException;
import com.golballogic.usersdemo.exception.UserCreationException;
import com.golballogic.usersdemo.repository.PhoneRepository;
import com.golballogic.usersdemo.repository.UserRepository;
import com.golballogic.usersdemo.security.AuthCredentials;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Instant;
import java.util.ArrayList;
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
    @InjectMocks
    UserService userService;


    @Test
    public void saveUserSuccessTest() {
        PhoneDTO phoneDto = PhoneDTO.builder()
                .number(123L).build();
        List<PhoneDTO> phones = new ArrayList<>();
        phones.add(phoneDto);

        CreateUserRequest userRequest = CreateUserRequest
                .builder()
                .email("test@test.com")
                .password("Password21")
                .phones(phones)
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

        CreateUserRequest userRequest = CreateUserRequest
                .builder()
                .email("test.com")
                .password("Password21")
                .build();

        userService.saveUser(userRequest);
    }

    @Test(expected = UserCreationException.class)
    public void saveUserBadPasswordTest() {

        CreateUserRequest userRequest = CreateUserRequest
                .builder()
                .email("test@test.com")
                .password("password2")
                .build();

        userService.saveUser(userRequest);
    }

    @Test(expected = UserCreationException.class)
    public void saveUserAlreadyExists() {

        CreateUserRequest userRequest = CreateUserRequest
                .builder()
                .email("test@test.com")
                .password("Password21")
                .build();
        User user = modelMapper.map(userRequest, User.class);
        when(userRepository.findOneUserByEmail(userRequest.getEmail())).thenReturn(Optional.of(user));

        userService.saveUser(userRequest);

    }

    @Test
    public void loginFindsUser() {
        AuthCredentials authCredentials = AuthCredentials.builder()
                .email("test@test.com")
                .password("Password21")
                .build();
        PhoneDTO phoneDto = PhoneDTO.builder()
                .number(123L).build();
        List<PhoneDTO> phones = new ArrayList<>();
        phones.add(phoneDto);


        UserDto userDto = UserDto
                .builder()
                .email("test@test.com")
                .password("Password21")
                .phones(phones)
                .created(Date.from(Instant.now()))
                .name("name")
                .lastLogin(Date.from(Instant.now()))
                .build();
        User user = modelMapper.map(userDto, User.class);

        when(userRepository.save(any())).thenReturn(user);
        when(userRepository.findOneUserByEmail(userDto.getEmail())).thenReturn(Optional.of(user));

        UserDto response = userService.login(userDto.getEmail(), authCredentials);

        assertEquals(response.getEmail(), userDto.getEmail());
        assertNotNull(response.getToken());
    }

    @Test(expected = AuthenticationException.class)
    public void loginBadPassword() {
        AuthCredentials authCredentials = AuthCredentials.builder()
                .email("test@test.com")
                .password("Password22")
                .build();
        PhoneDTO phoneDto = PhoneDTO.builder()
                .number(123L).build();
        List<PhoneDTO> phones = new ArrayList<>();
        phones.add(phoneDto);


        UserDto userDto = UserDto
                .builder()
                .email("test@test.com")
                .password("Password21")
                .phones(phones)
                .created(Date.from(Instant.now()))
                .name("name")
                .lastLogin(Date.from(Instant.now()))
                .build();
        User user = modelMapper.map(userDto, User.class);

        when(userRepository.findOneUserByEmail(userDto.getEmail())).thenReturn(Optional.of(user));

        userService.login(userDto.getEmail(), authCredentials);
    }

    @Test(expected = AuthenticationException.class)
    public void loginBadEmail() {
        AuthCredentials authCredentials = AuthCredentials.builder()
                .email("test1@test.com")
                .password("Password21")
                .build();
        PhoneDTO phoneDto = PhoneDTO.builder()
                .number(123L).build();
        List<PhoneDTO> phones = new ArrayList<>();
        phones.add(phoneDto);


        UserDto userDto = UserDto
                .builder()
                .email("test@test.com")
                .password("Password21")
                .phones(phones)
                .created(Date.from(Instant.now()))
                .name("name")
                .lastLogin(Date.from(Instant.now()))
                .build();
        User user = modelMapper.map(userDto, User.class);

        when(userRepository.findOneUserByEmail(userDto.getEmail())).thenReturn(Optional.of(user));

        userService.login(userDto.getEmail(), authCredentials);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loginDoesNotFindsUser() {
        UserDto userDto = UserDto
                .builder()
                .email("test@test.com")
                .password("Password21")
                .created(Date.from(Instant.now()))
                .name("name")
                .lastLogin(Date.from(Instant.now()))
                .build();

        when(userRepository.findOneUserByEmail(userDto.getEmail())).thenReturn(Optional.empty());

        userService.login(userDto.getEmail(), new AuthCredentials());
    }

}
