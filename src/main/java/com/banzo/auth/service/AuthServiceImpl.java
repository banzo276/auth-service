package com.banzo.auth.service;

import com.banzo.auth.dto.UserDto;
import com.banzo.auth.exception.BadRequestException;
import com.banzo.auth.jwt.JwtTokenProvider;
import com.banzo.auth.mappers.UserMapper;
import com.banzo.auth.model.Role;
import com.banzo.auth.model.User;
import com.banzo.auth.payload.JwtResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

  private final UserService userService;
  private final RoleService roleService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  @Override
  public JwtResponse login(String username, String password) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password));
      User authUser = userService.findByUsername(username).get();
      String token = jwtTokenProvider.generateToken(username, authUser.getRoles());

      return new JwtResponse(token, authUser.getId(), authUser.getUsername());
    } catch (Exception e) {
      throw new BadRequestException("Invalid credentials");
    }
  }

  @Override
  public JwtResponse register(String username, String password) {
    if (userService.findByUsername(username).isEmpty()) {

      String encodedPassword = passwordEncoder.encode(password);

      Role defaultRole = roleService.findByName("ROLE_VIEWER").get();

      User user =
          User.builder()
              .username(username)
              .password(encodedPassword)
              .roles(Collections.singleton(defaultRole))
              .build();

      userService.saveOrUpdate(user);
      String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRoles());
      return new JwtResponse(token, user.getId(), user.getUsername());
    } else {
      throw new BadRequestException("Username is already in use");
    }
  }

  @Override
  public UserDto currentUser(HttpServletRequest request) {
    return userMapper.userToUserDto(
        userService
            .findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request)))
            .get());
  }

  @Scheduled(fixedRate = 60000)
  @Override
  public void resetFailedLogins() {

    log.info("Checking for locked accounts");

    Iterable<User> users = userService.findAll();

    users.forEach(
        user -> {
          if (!user.getEnabled() && user.getFailedLoginAttempts() > 0) {
            log.info("Resetting failed attempts for user: " + user.getUsername());
            user.setFailedLoginAttempts(0);
            user.setEnabled(true);
            userService.saveOrUpdate(user);
          }
        });
  }
}
