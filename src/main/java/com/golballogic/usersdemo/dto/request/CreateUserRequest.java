package com.golballogic.usersdemo.dto.request;

import com.golballogic.usersdemo.dto.PhoneDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String password;
    private List<PhoneDTO> phones;

}
