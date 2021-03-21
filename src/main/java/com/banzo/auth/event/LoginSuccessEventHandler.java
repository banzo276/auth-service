package com.banzo.auth.event;

import com.banzo.auth.model.User;
import com.banzo.auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoginSuccessEventHandler implements ApplicationListener<LoginSuccessEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private UserService userService;

    public LoginSuccessEventHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(LoginSuccessEvent event) {

        Authentication authentication = (Authentication) event.getSource();
        logger.info("Authentication successful");
        updateUserAccount(authentication);
    }

    private void updateUserAccount(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userService.findByUsername(userDetails.getUsername());

        if (user.isPresent()) {
            User foundUser = user.get();

            if (foundUser.getFailedLoginAttempts() > 0) {
                logger.info("Login successful, resetting failed attempts");
                foundUser.setFailedLoginAttempts(0);

                userService.saveOrUpdate(foundUser);
            }
        }
    }
}
