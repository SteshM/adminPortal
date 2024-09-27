package com.example.Admin.auth;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AuthenticationRequest {
    private String email;
    String password;
}
