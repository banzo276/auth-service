package com.banzo.auth.service;

import com.banzo.auth.model.Role;

public interface RoleService {

  Role findByName(String name);
}
