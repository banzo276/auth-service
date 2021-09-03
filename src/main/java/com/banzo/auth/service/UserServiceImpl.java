package com.banzo.auth.service;

import com.banzo.auth.exception.RecordNotFoundException;
import com.banzo.auth.model.User;
import com.banzo.auth.model.UserPrincipal;
import com.banzo.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public User findByUsername(String username) {
    return userRepository.findByUsername(username)
            .orElseThrow(() -> new RecordNotFoundException("User not found, username: " + username));
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .map(UserPrincipal::new)
        .orElseThrow(() -> new RecordNotFoundException("User not found, username: " + username));
  }

  @Override
  @Transactional
  public User saveOrUpdate(User user) {
    return userRepository.save(user);
  }

  @Override
  @Transactional
  public Iterable<User> findAll() {
    return userRepository.findAll();
  }
}
