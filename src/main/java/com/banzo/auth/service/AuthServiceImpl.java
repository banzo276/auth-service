package com.banzo.auth.service;

import com.banzo.auth.jwt.JwtTokenProvider;
import com.banzo.auth.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(String username, String password) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    username, password));
            User authUser = userService.findByUsername(username).get();
            String token = jwtTokenProvider.generateToken(username, authUser.getRoles());

            return token;
        } catch (Exception e) {
            throw new Exception(e);
        }
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
