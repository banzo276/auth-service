package com.banzo.auth.aspect;

import com.banzo.auth.event.LoginFailureEvent;
import com.banzo.auth.event.LoginFailureEventPublisher;
import com.banzo.auth.event.LoginSuccessEvent;
import com.banzo.auth.event.LoginSuccessEventPublisher;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoginAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private LoginFailureEventPublisher loginFailureEventPublisher;
    private LoginSuccessEventPublisher loginSuccessEventPublisher;

    @Autowired
    public void setLoginFailureEventPublisher(LoginFailureEventPublisher loginFailureEventPublisher) {
        this.loginFailureEventPublisher = loginFailureEventPublisher;
    }

    @Autowired
    public void setLoginSuccessEventPublisher(LoginSuccessEventPublisher loginSuccessEventPublisher) {
        this.loginSuccessEventPublisher = loginSuccessEventPublisher;
    }

    @Pointcut("execution(* org.springframework.security.authentication.AuthenticationProvider.authenticate(..))")
    public void processAuthentication(){}

    @Before("processAuthentication() && args(authentication)")
    public void logBeforeAuthentication(Authentication authentication) {
        logger.info("Trying to authenticate user: " + (String) authentication.getPrincipal());
    }

    @AfterReturning(value = "processAuthentication()", returning = "authentication")
    public void logAuthenticationSuccess(Authentication authentication) {
        logger.info("User is authenticated: " + authentication.isAuthenticated());
        loginSuccessEventPublisher.publish(new LoginSuccessEvent(authentication));
    }

    @AfterThrowing("processAuthentication() && args(authentication)")
    public void logAuthenticationFailure(Authentication authentication) {
        loginFailureEventPublisher.publish(new LoginFailureEvent(authentication));
    }
}
