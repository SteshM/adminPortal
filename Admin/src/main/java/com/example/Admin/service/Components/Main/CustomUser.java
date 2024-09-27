package com.example.Admin.service.Components.Main;

import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CustomUser {
    private String username;
    private Collection<SimpleGrantedAuthority> grantedAuthorities;
    private Long depotId;
}
