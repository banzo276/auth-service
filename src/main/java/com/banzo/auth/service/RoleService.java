package com.banzo.auth.service;

import com.banzo.auth.model.Role;

import java.util.Optional;

public interface RoleService {

    Optional<Role> findByName(String name);
}
