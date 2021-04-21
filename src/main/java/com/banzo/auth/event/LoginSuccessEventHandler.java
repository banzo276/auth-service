package com.banzo.auth.event;

import com.banzo.auth.model.User;
import com.banzo.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginSuccessEventHandler implements ApplicationListener<LoginSuccessEvent> {

    private final UserService userService;

    @Override
    public void onApplicationEvent(LoginSuccessEvent event) {

        Authentication authentication = (Authentication) event.getSource();
        log.info("Authentication successful");
        updateUserAccount(authentication);
    }

    private void updateUserAccount(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userService.findByUsername(userDetails.getUsername());

        if (user.isPresent()) {
            User foundUser = user.get();

            if (foundUser.getFailedLoginAttempts() > 0) {
                log.info("Login successful, resetting failed attempts");
                foundUser.setFailedLoginAttempts(0);

                userService.saveOrUpdate(foundUser);
            }
        }
    }
}
