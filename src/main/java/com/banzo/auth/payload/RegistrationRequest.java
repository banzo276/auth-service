package com.banzo.auth.payload;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RegistrationRequest {

  String username;
  String password;
}
