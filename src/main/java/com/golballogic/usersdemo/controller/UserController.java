package com.golballogic.usersdemo.controller;

import com.golballogic.usersdemo.dto.request.CreateUserRequest;
import com.golballogic.usersdemo.dto.response.UserCreationResponse;
import com.golballogic.usersdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController()
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(produces = "application/json")
    public ResponseEntity<?> saveUser(@RequestBody CreateUserRequest userRequest) {
        UserCreationResponse user =  userService.saveUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping(value = "/login", produces = "application/json")
    public ResponseEntity<?> login(Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByEmail(principal.getName()));
    }
}
