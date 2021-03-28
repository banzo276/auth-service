package com.banzo.auth.service;

import com.banzo.auth.model.Role;
import com.banzo.auth.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
}
