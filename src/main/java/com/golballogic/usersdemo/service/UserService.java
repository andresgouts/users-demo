package com.golballogic.usersdemo.service;

import com.golballogic.usersdemo.domain.User;
import com.golballogic.usersdemo.dto.UserDto;
import com.golballogic.usersdemo.dto.request.CreateUserRequest;
import com.golballogic.usersdemo.dto.response.UserCreationResponse;
import com.golballogic.usersdemo.exception.UserCreationException;
import com.golballogic.usersdemo.repository.PhoneRepository;
import com.golballogic.usersdemo.repository.UserRepository;
import com.golballogic.usersdemo.security.TokenUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PASSWORD_REGEX = "^(?=(?:[^A-Z]*[A-Z]){1})(?=(?:[^0-9]*[0-9]){2})[A-Za-z0-9]{8,12}$";
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository,
                       PhoneRepository phoneRepository,
                       PasswordEncoder passwordEncoder,
                       ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.phoneRepository = phoneRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public UserCreationResponse saveUser(CreateUserRequest userRequest) {
        validateUserCreationData(userRequest);

        User user = modelMapper.map(userRequest, User.class);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setIsActive(true);


        user.setLastLogin(Date.from(Instant.now()));

        User savedUser = userRepository.save(user);

        if (user.getPhones() != null && !user.getPhones().isEmpty()) {
            user.getPhones().forEach(phone -> {
                phone.setUser(savedUser);
            });
            phoneRepository.saveAll(user.getPhones());
        }


        String token = TokenUtils.createToken(savedUser.getName(), savedUser.getEmail());
        UserCreationResponse response = modelMapper.map(savedUser, UserCreationResponse.class);
        response.setToken("Bearer " + token);
        return response;

    }

    public void validateUserCreationData(CreateUserRequest userRequest) {
        validateEmail(userRequest.getEmail());
        validatePasswordFormat(userRequest.getPassword());

        if (userRepository.findOneUserByEmail(userRequest.getEmail()).isPresent()) {
            throw new UserCreationException("User Already exists");
        }
    }

    public UserDto login(String email) {
        User user =  userRepository
                .findOneUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setLastLogin(Date.from(Instant.now()));

        userRepository.save(user);

        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setToken("Bearer "  + TokenUtils.createToken(user.getName(), user.getEmail()));
        return userDto;
    }
    
    private void validateEmail(String emailAddress) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(emailAddress);

        if(!matcher.matches()) {
            throw new UserCreationException("Email address is invalid");
        };
    }

    private void validatePasswordFormat(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);

        if(!matcher.matches()) {
            throw new UserCreationException("Password is invalid");
        }
    }
}
