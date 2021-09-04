package com.banzo.auth.service;

import com.banzo.auth.exception.RecordNotFoundException;
import com.banzo.auth.model.UserPrincipal;
import com.banzo.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .map(UserPrincipal::new)
        .orElseThrow(() -> new RecordNotFoundException("User not found, username: " + username));
  }
}
