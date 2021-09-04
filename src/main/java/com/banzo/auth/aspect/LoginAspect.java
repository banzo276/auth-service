package com.banzo.auth.aspect;

import com.banzo.auth.event.LoginFailureEvent;
import com.banzo.auth.event.LoginFailureEventPublisher;
import com.banzo.auth.event.LoginSuccessEvent;
import com.banzo.auth.event.LoginSuccessEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class LoginAspect {

  private final LoginFailureEventPublisher loginFailureEventPublisher;
  private final LoginSuccessEventPublisher loginSuccessEventPublisher;

  @Pointcut(
      "execution(* org.springframework.security.authentication.AuthenticationProvider.authenticate(..))")
  public void processAuthentication() {}

  @Before("processAuthentication() && args(authentication)")
  public void logBeforeAuthentication(Authentication authentication) {
    log.info("Trying to authenticate user: " + (String) authentication.getPrincipal());
  }

  @AfterReturning(value = "processAuthentication()", returning = "authentication")
  public void logAuthenticationSuccess(Authentication authentication) {
    log.info("User is authenticated: " + authentication.isAuthenticated());
    loginSuccessEventPublisher.publish(new LoginSuccessEvent(authentication));
  }

  @AfterThrowing("processAuthentication() && args(authentication)")
  public void logAuthenticationFailure(Authentication authentication) {
    loginFailureEventPublisher.publish(new LoginFailureEvent(authentication));
  }
}
