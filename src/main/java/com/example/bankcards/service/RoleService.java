package com.example.bankcards.service;

import com.example.bankcards.entity.Role;
import com.example.bankcards.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    private final  RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").get();
    }

    public Optional<Role> findRole(String roleName) {
        return roleRepository.findByName(roleName);
    }
}
