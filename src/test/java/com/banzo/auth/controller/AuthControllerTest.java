package com.banzo.auth.controller;

import com.banzo.auth.jwt.JwtTokenProvider;
import com.banzo.auth.payload.LoginRequest;
import com.banzo.auth.model.User;
import com.banzo.auth.model.UserPrincipal;
import com.banzo.auth.service.AuthServiceImpl;
import com.banzo.auth.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureTestDatabase
class AuthControllerTest {

  @MockBean AuthServiceImpl authService;

  @MockBean UserService userService;

  @MockBean AuthenticationManager authenticationManager;

  @MockBean JwtTokenProvider jwtTokenProvider;

  @Autowired MockMvc mockMvc;

  @Autowired ObjectMapper objectMapper;

  @AfterEach
  void tearDown() {
    reset(userService);
  }

  @Test
  void login() throws Exception {

    var loginRequest = LoginRequest.builder().username("admin").password("admin").build();
    User user = getUser();
    UserDetails userDetails = new UserPrincipal(user);
    String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

    given(authenticationManager.authenticate(any()))
        .willReturn(
            new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword()));

    mockMvc
        .perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestJson)
                .with(csrf()))
        .andExpect(status().isOk());
  }

  private User getUser() {

    var user = User.builder().id(1L).username("admin").password("admin").build();

    return user;
  }
}
