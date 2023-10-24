package com.golballogic.usersdemo.dto.request;

import com.golballogic.usersdemo.domain.User;
import com.golballogic.usersdemo.dto.PhoneDTO;
import com.sun.istack.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateUserRequest {
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String password;
    private List<PhoneDTO> phones;

}
