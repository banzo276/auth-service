package com.banzo.auth.payload;

import lombok.*;

@Value
@Builder
public class LoginRequest {

  String username;
  String password;
}
