package com.golballogic.usersdemo.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {
    private UUID id;
    private Date created;
    private Date lastLogin;
    private String token;
    private boolean isActive;
    private String name;
    private String email;
    private String password;
    private List<PhoneDTO> phones;
}
