package com.banzo.auth.controller;

import com.banzo.auth.dto.UserDto;
import com.banzo.auth.payload.JwtResponse;
import com.banzo.auth.payload.LoginRequest;
import com.banzo.auth.payload.RegistrationRequest;
import com.banzo.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@RequestBody RegistrationRequest registrationRequest) {
        JwtResponse jwtResponse = authService.register(
                registrationRequest.getUsername(), registrationRequest.getPassword());
        return new ResponseEntity<>(jwtResponse, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<UserDto> currentUser(HttpServletRequest request) {
        return new ResponseEntity<>(modelMapper.map(
                authService.currentUser(request), UserDto.class),
                HttpStatus.OK);
    }
}
