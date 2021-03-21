package com.banzo.auth.service;

import com.banzo.auth.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private UserService userService;

    public LoginServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Scheduled(fixedRate = 60000)
    @Override
    public void resetFailedLogins() {

        logger.info("Checking for locked accounts");

        Iterable<User> users = userService.findAll();

        users.forEach(user -> {
            if (!user.getEnabled() && user.getFailedLoginAttempts() > 0) {
                logger.info("Resetting failed attempts for user: " + user.getUsername());
                user.setFailedLoginAttempts(0);
                user.setEnabled(true);
                userService.saveOrUpdate(user);
            }
        });
    }
}
