package com.banzo.auth.service;

import com.banzo.auth.model.User;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {

    void resetFailedLogins();
    String login(String username, String password);
    String register(User user);
    User currentUser(HttpServletRequest request);
}
