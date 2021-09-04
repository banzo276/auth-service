package com.banzo.auth.service;

import com.banzo.auth.dto.UserDto;
import com.banzo.auth.exception.AccessDeniedException;
import com.banzo.auth.exception.BadRequestException;
import com.banzo.auth.exception.RecordNotFoundException;
import com.banzo.auth.jwt.JwtTokenProvider;
import com.banzo.auth.mappers.UserMapper;
import com.banzo.auth.model.Role;
import com.banzo.auth.model.User;
import com.banzo.auth.payload.JwtResponse;
import com.banzo.auth.repository.RoleRepository;
import com.banzo.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  private static final String BAD_CREDENTIALS = "Invalid credentials";
  private static final String DEFAULT_ROLE = "ROLE_VIEWER";

  @Transactional
  @Override
  public JwtResponse login(String username, String password) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password));

      return userRepository
          .findByUsername(username)
          .map(
              user -> {
                String token = jwtTokenProvider.generateToken(username, user.getRoles());
                return JwtResponse.builder()
                    .token(token)
                    .userId(user.getId())
                    .username(user.getUsername())
                    .build();
              })
          .orElseThrow(() -> new AccessDeniedException(BAD_CREDENTIALS));

    } catch (AuthenticationException e) {
      throw new AccessDeniedException(BAD_CREDENTIALS);
    }
  }

  @Transactional
  @Override
  public JwtResponse register(String username, String password) {
    if (userRepository.findByUsername(username).isEmpty()) {

      String encodedPassword = passwordEncoder.encode(password);
      Role defaultRole =
          roleRepository
              .findByName(DEFAULT_ROLE)
              .orElseThrow(
                  () -> new RecordNotFoundException("Role not found, role name: " + DEFAULT_ROLE));

      User user =
          userRepository.save(
              User.builder()
                  .username(username)
                  .password(encodedPassword)
                  .roles(Collections.singleton(defaultRole))
                  .build());

      String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRoles());
      return JwtResponse.builder()
          .token(token)
          .userId(user.getId())
          .username(user.getUsername())
          .build();
    } else {
      throw new BadRequestException("Username already in use");
    }
  }

  @Transactional(readOnly = true)
  @Override
  public UserDto currentUser(HttpServletRequest request) {
    return userMapper.userToUserDto(
        userRepository
            .findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request)))
            .orElseThrow(() -> new BadRequestException("Invalid user data")));
  }

  @Scheduled(fixedRate = 60000)
  @Transactional
  @Override
  public void resetFailedLogins() {

    log.info("Checking for locked accounts");

    userRepository.findAll().stream()
        .filter(user -> !user.getEnabled() && user.getFailedLoginAttempts() > 0)
        .map(
            user -> {
              log.info("Resetting failed attempts for user: " + user.getUsername());
              user.setFailedLoginAttempts(0);
              user.setEnabled(true);
              return user;
            })
        .forEach(userRepository::save);
  }
}
