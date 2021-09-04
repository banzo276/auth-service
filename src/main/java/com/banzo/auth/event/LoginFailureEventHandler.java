package com.banzo.auth.event;

import com.banzo.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginFailureEventHandler implements ApplicationListener<LoginFailureEvent> {

  private final UserRepository userRepository;

  @Override
  public void onApplicationEvent(LoginFailureEvent event) {

    Authentication authentication = (Authentication) event.getSource();
    log.info("Authentication failed for user: " + (String) authentication.getPrincipal());
    updateUserAccount(authentication);
  }

  private void updateUserAccount(Authentication authentication) {

    userRepository
        .findByUsername((String) authentication.getPrincipal())
        .ifPresent(
            user -> {
              user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
              log.info("Invalid password, failed login attempts: " + user.getFailedLoginAttempts());

              if (user.getFailedLoginAttempts() > 5) {
                user.setEnabled(false);
                log.info("User account: " + user.getUsername() + " is locked.");
              }

              userRepository.save(user);
            });
  }
}
