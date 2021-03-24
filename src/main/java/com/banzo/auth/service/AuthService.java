package com.banzo.auth.service;

public interface AuthService {

    void resetFailedLogins();
    String login(String username, String password) throws Exception;
}
