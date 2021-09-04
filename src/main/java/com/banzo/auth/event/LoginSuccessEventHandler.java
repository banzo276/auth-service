package com.banzo.auth.event;

import com.banzo.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginSuccessEventHandler implements ApplicationListener<LoginSuccessEvent> {

  private final UserRepository userRepository;

  @Override
  public void onApplicationEvent(LoginSuccessEvent event) {

    Authentication authentication = (Authentication) event.getSource();
    log.info("Authentication successful");
    updateUserAccount(authentication);
  }

  private void updateUserAccount(Authentication authentication) {

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    userRepository
        .findByUsername(userDetails.getUsername())
        .ifPresent(
            user -> {
              if (user.getFailedLoginAttempts() > 0) {
                log.info("Login successful, resetting failed attempts");
                user.setFailedLoginAttempts(0);

                userRepository.save(user);
              }
            });
  }
}
