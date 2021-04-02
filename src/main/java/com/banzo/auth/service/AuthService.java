package com.banzo.auth.service;

import com.banzo.auth.model.User;
import com.banzo.auth.payload.JwtResponse;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {

    void resetFailedLogins();
    JwtResponse login(String username, String password);
    JwtResponse register(String username, String password);
    User currentUser(HttpServletRequest request);
}
