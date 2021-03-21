package com.banzo.auth.event;

import com.banzo.auth.model.User;
import com.banzo.auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoginFailureEventHandler implements ApplicationListener<LoginFailureEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private UserService userService;

    public LoginFailureEventHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(LoginFailureEvent event) {

        Authentication authentication = (Authentication) event.getSource();
        logger.info("Authentication failed for user: " + (String) authentication.getPrincipal());
        updateUserAccount(authentication);
    }

    private void updateUserAccount(Authentication authentication) {

        Optional<User> user = userService.findByUsername((String) authentication.getPrincipal());

        if (user.isPresent()) {
            User foundUser = user.get();
            foundUser.setFailedLoginAttempts(foundUser.getFailedLoginAttempts() + 1);
            logger.info("Invalid password, failed login attempts: " + foundUser.getFailedLoginAttempts());

            if (foundUser.getFailedLoginAttempts() > 5) {
                foundUser.setEnabled(false);
                logger.info("User account: " + foundUser.getUsername() + " is locked.");
            }

            userService.saveOrUpdate(foundUser);
        }
    }
}
