package com.golballogic.usersdemo.service;

import com.golballogic.usersdemo.domain.Phone;
import com.golballogic.usersdemo.domain.User;
import com.golballogic.usersdemo.dto.request.CreateUserRequest;
import com.golballogic.usersdemo.dto.request.PhoneDTO;
import com.golballogic.usersdemo.dto.response.UserCreationResponse;
import com.golballogic.usersdemo.repository.PhoneRepository;
import com.golballogic.usersdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;

    @Autowired
    public UserService(UserRepository userRepository, PhoneRepository phoneRepository) {
        this.userRepository = userRepository;
        this.phoneRepository = phoneRepository;
    }

    public UserCreationResponse saveUser(CreateUserRequest userRequest) {
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
}
