package com.example.Admin.config;


import com.example.Admin.service.Components.Main.CustomUser;
import com.example.Admin.service.Components.utils.JwtGenerator2;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtGenerator2 jwtGenerator2;
    private String jwt = "";
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        ArrayList<String> pathsAllowed = new ArrayList<>();
        pathsAllowed.add("/api/v1/auth/allow");
        pathsAllowed.add("/api/v1/auth/all");
        pathsAllowed.add("/image/getPic");
        pathsAllowed.add("/v1/general");

        if(this.pathAllowed(pathsAllowed, request.getServletPath())
                || request.getServletPath().contains("docs")||
                request.getServletPath().contains("/swagger-ui")){
            log.info("allowed "+request.getServletPath());
            filterChain.doFilter(request, response);
        }else{
            final String authHeader= request.getHeader("Authorization");
            if(authHeader == null){
                this.badResponse(300,"No token provided", response);
            }

            if (authHeader == null||!authHeader.startsWith("Bearer ")){
                this.badResponse(300,"No bearer token", response);
                return;

            }
            jwt= authHeader.substring(7);
            CustomUser user = null;
            try{
                user = jwtGenerator2.getUser(jwt);
            }catch(Exception e){
                this.badResponse(303,e.getLocalizedMessage(), response);
                return;
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), null,user.getGrantedAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request,response);
        }


    }

    private void badResponse(int status,String message, HttpServletResponse response){
        log.warn("there was a problem filtering: "+ message);
        JsonMapper jsonMapper = new JsonMapper();
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("success", false);
        map.put("message", message);
        response.setContentType("application/json");
        try{
            jsonMapper.writeValue(response.getOutputStream(), map);
        }catch(Exception e){
            log.warn(e.getLocalizedMessage());
        }
    }

    private boolean pathAllowed(ArrayList<String> allowedPaths, String currentPath){
        for(String path: allowedPaths){
            if(currentPath.startsWith(path)){
                return true;
            }
        }
        return false;
    }
}

