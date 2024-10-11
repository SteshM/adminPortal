package com.example.Admin.auth;

import com.example.Admin.dto.MinimalRes;
import com.example.Admin.dto.RegisterDto;
import com.example.Admin.dto.ResetPasswordDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    @Autowired
    AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<Object> register  (@RequestBody RegisterDto registerDto){
        return ResponseEntity.ok(service.register(registerDto));

    }
    @PostMapping("/allow/login")
    public ResponseEntity<AuthenticationResponse> authenticate  (@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }
    @PostMapping("/allow/otp")
    public MinimalRes otpPassword(@RequestBody ResetPasswordDto resetPassword){
        return service.changePassword(resetPassword);
    }

    @PostMapping("/changePassword")
    public MinimalRes updatePassword(@RequestBody ResetPasswordDto resetPasswordDto){
        return service.changePassword(resetPasswordDto);
    }

    @GetMapping("/allow/resetPass/{email}")
    public Map<String, Object> resetPass(@PathVariable String email){
        return service.resetPassword(email);
    }

    @PostMapping("/allow/registerRetailer")
    public ResponseEntity<Object> registerRetailer(@RequestBody RegisterDto registerDto){
        return new ResponseEntity<Object>(service.registerRetailer(registerDto), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/allow/refresh")
    public AuthenticationResponse refreshToken( HttpServletRequest request){
        return service.refreshToken(request);
    }

}
