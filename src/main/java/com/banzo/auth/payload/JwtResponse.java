package com.banzo.auth.payload;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class JwtResponse {

  String token;
  Long userId;
  String username;
}
