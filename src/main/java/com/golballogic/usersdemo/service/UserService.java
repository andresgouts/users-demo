package com.golballogic.usersdemo.service;

import com.golballogic.usersdemo.domain.Phone;
import com.golballogic.usersdemo.domain.User;
import com.golballogic.usersdemo.dto.request.CreateUserRequest;
import com.golballogic.usersdemo.dto.request.PhoneDTO;
import com.golballogic.usersdemo.dto.response.UserCreationResponse;
import com.golballogic.usersdemo.exception.UserCreationException;
import com.golballogic.usersdemo.repository.PhoneRepository;
import com.golballogic.usersdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PASSWORD_REGEX = "^(?=(?:[^A-Z]*[A-Z]){1})(?=(?:[^0-9]*[0-9]){2})[A-Za-z0-9]{8,12}$";
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;

    @Autowired
    public UserService(UserRepository userRepository, PhoneRepository phoneRepository) {
        this.userRepository = userRepository;
        this.phoneRepository = phoneRepository;
    }

    public UserCreationResponse saveUser(CreateUserRequest userRequest) {
        validateEmail(userRequest.getEmail());
        validatePasswordFormat(userRequest.getPassword());

        User savedUser = userRepository.save(userRequest.toUserEntity());

        List<Phone> phones = userRequest.getPhones().stream()
                .map(PhoneDTO::toPhoneEntity)
                .collect(Collectors.toList());

        phones.forEach(phone -> {
            phone.setUser(savedUser);
        });

        phoneRepository.saveAll(phones);

        UserCreationResponse userResponse = new UserCreationResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setName(savedUser.getName());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setToken(savedUser.getToken());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        userResponse.setIsActive(savedUser.getActive());
        userResponse.setLastLogin(savedUser.getLastLogin());

        return userResponse;

    }
    
    private void validateEmail(String emailAddress) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(emailAddress);

        if(!matcher.matches()) {
            throw new UserCreationException("Email address is invalid");
        };
    }
    
    //TODO need to fix regex to allow only one capital letter and 2 numbers
    private void validatePasswordFormat(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);

        if(!matcher.matches()) {
            throw new UserCreationException("Password is invalid");
        }
    }
}
