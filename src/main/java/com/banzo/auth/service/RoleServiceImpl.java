package com.banzo.auth.service;

import com.banzo.auth.exception.RecordNotFoundException;
import com.banzo.auth.model.Role;
import com.banzo.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;

  @Override
  @Transactional
  public Role findByName(String name) {
    return roleRepository
        .findByName(name)
        .orElseThrow(() -> new RecordNotFoundException("Role not found, role name: " + name));
  }
}
