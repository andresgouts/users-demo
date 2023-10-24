package com.golballogic.usersdemo.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserCreationResponse {
    private UUID id;
    private String name;
    private String email;
    private Date lastLogin;
    private Boolean isActive;
    private String token;
    private Date createdAt;
}
