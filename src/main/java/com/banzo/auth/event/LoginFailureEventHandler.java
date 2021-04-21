package com.banzo.auth.event;

import com.banzo.auth.model.User;
import com.banzo.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginFailureEventHandler implements ApplicationListener<LoginFailureEvent> {

    private final UserService userService;

    @Override
    public void onApplicationEvent(LoginFailureEvent event) {

        Authentication authentication = (Authentication) event.getSource();
        log.info("Authentication failed for user: " + (String) authentication.getPrincipal());
        updateUserAccount(authentication);
    }

    private void updateUserAccount(Authentication authentication) {

        Optional<User> user = userService.findByUsername((String) authentication.getPrincipal());

        if (user.isPresent()) {
            User foundUser = user.get();
            foundUser.setFailedLoginAttempts(foundUser.getFailedLoginAttempts() + 1);
            log.info("Invalid password, failed login attempts: " + foundUser.getFailedLoginAttempts());

            if (foundUser.getFailedLoginAttempts() > 5) {
                foundUser.setEnabled(false);
                log.info("User account: " + foundUser.getUsername() + " is locked.");
            }

            userService.saveOrUpdate(foundUser);
        }
    }
}
