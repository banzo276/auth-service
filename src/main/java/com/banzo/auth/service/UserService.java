package com.banzo.auth.service;

import com.banzo.auth.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<User> findByUsername(String username);
    User saveOrUpdate(User user);
    Iterable<User> findAll();
}
