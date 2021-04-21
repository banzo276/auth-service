package com.banzo.auth.service;

import com.banzo.auth.model.Role;
import com.banzo.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
}
