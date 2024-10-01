package com.example.Admin.service.Components.utils;

import com.example.Admin.service.Components.Main.CustomUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
@Component
public class JwtGenerator2 {
    private static final String SECRET_KEY="e03c24c66303d84372e3c4a057a93e70664f4501e7116d6b5ae544173fa26b5a";

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String token(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*2))
                .setIssuer("DISTRIBUTOR")
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String refreshToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*2))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setIssuer("DISTRIBUTOR")
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @SuppressWarnings("unchecked")
    public CustomUser getUser(String token) throws Exception{
        Claims claims = this.getClaims(token);

        List<String> roles= (List<String>) claims.get("roles");
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for(String role: roles){
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return CustomUser.builder()
                .username(claims.getSubject())
                .grantedAuthorities(authorities)
                .build();
    }

    public Claims getClaims(String token) throws Exception{
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Object getSelectedDepot(String token) throws Exception{
        Object depotId = this.getClaims(token).get("depotId");
        return depotId;
    }
}

