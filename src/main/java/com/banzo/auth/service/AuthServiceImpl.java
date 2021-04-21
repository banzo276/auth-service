package com.banzo.auth.service;

import com.banzo.auth.exception.BadRequestException;
import com.banzo.auth.jwt.JwtTokenProvider;
import com.banzo.auth.model.Role;
import com.banzo.auth.model.User;
import com.banzo.auth.payload.JwtResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private UserService userService;
    private RoleService roleService;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public JwtResponse login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    username, password));
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

            User user = User.builder()
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
    public User currentUser(HttpServletRequest request) {
        return userService.findByUsername(
                jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request)))
                .get();
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
