package com.banzo.auth.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class JwtResponse {

  private String token;
  private Long id;
  private String username;
}
