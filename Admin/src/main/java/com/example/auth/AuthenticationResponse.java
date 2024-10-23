package com.example.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class AuthenticationResponse {
    private int status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> roles;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refreshToken;
    private Object object;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String picture;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean cloud;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String publicId;

}
