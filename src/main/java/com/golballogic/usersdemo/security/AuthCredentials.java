package com.golballogic.usersdemo.security;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthCredentials {

    private String email;
    private String password;
}
