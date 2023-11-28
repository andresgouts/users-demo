package com.globallogic.usersdemo.service

import com.golballogic.usersdemo.domain.User
import com.golballogic.usersdemo.dto.request.CreateUserRequest
import com.golballogic.usersdemo.dto.response.UserCreationResponse
import com.golballogic.usersdemo.repository.PhoneRepository
import com.golballogic.usersdemo.repository.UserRepository
import com.golballogic.usersdemo.service.UserService
import org.modelmapper.ModelMapper
import spock.lang.Specification

class UserServiceTest extends Specification {

    def USER_REQUEST = CreateUserRequest.builder()
            .email("test@test.com")
            .password("Password21")
            .build();
    def userModel

    def userRepository = Mock(UserRepository)
    def phoneRepository = Mock(PhoneRepository)
    def modelMapper = Spy(ModelMapper)
    def userService = new UserService(userRepository, phoneRepository, modelMapper);

    def setup() {
         userModel = modelMapper.map(USER_REQUEST, User.class)
    }


    def "new user is saved successfully"() {
        given:
        userRepository.save(_) >> userModel
        userRepository.findOneUserByEmail(userModel.getEmail()) >> Optional.empty();

        when:
        UserCreationResponse response = userService.saveUser(USER_REQUEST)

        then:
        response.getEmail().equals(USER_REQUEST.getEmail())
        and:
        !response.getToken().isEmpty()


    }
}
