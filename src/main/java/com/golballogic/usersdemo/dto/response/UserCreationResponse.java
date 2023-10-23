package com.golballogic.usersdemo.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class UserCreationResponse {
    private UUID id;
    private String name;
    private String email;
    private Instant lastLogin;
    private Boolean isActive;
    private String token;
    private Instant createdAt;
}
