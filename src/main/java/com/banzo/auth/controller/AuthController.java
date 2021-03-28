package com.banzo.auth.controller;

import com.banzo.auth.model.LoginRequest;
import com.banzo.auth.model.RegistrationRequest;
import com.banzo.auth.model.Role;
import com.banzo.auth.model.User;
import com.banzo.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String jwtToken = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return new ResponseEntity<>(jwtToken, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest registrationRequest) {

        User user = User.builder()
                    .username(registrationRequest.getUsername())
                    .password(registrationRequest.getPassword())
                    .enabled(true)
                    .failedLoginAttempts(0)
                    .roles(Collections.singleton(Role.builder().name("ROLE_VIEWER").build()))
                    .build();

        String jwtToken = authService.register(user);
        return new ResponseEntity<>(jwtToken, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<User> currentUser(HttpServletRequest request) {
        return new ResponseEntity<>(authService.currentUser(request), HttpStatus.OK);
    }
}
