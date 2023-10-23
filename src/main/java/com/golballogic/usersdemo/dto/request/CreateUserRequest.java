package com.golballogic.usersdemo.dto.request;

import com.golballogic.usersdemo.domain.User;
import lombok.Data;

import java.util.List;

@Data
public class CreateUserRequest {
    private String name;
    private String email;
    private String password;
    private List<PhoneDTO> phones;

    public User toUserEntity() {
        User user = new User();
        user.setActive(true);
        user.setEmail(this.email);
        user.setName(this.email);
        user.setPassword(this.password);
        return user;
    }
}
