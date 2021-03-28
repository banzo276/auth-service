package com.banzo.auth.controller;

import com.banzo.auth.model.LoginRequest;
import com.banzo.auth.model.RegistrationRequest;
import com.banzo.auth.model.User;
import com.banzo.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
        String jwtToken = authService.register(
                registrationRequest.getUsername(), registrationRequest.getPassword());
        return new ResponseEntity<>(jwtToken, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<User> currentUser(HttpServletRequest request) {
        return new ResponseEntity<>(authService.currentUser(request), HttpStatus.OK);
    }
}
