package com.ctd.slacklite.auth.service;

import com.ctd.slacklite.auth.model.Role;
import com.ctd.slacklite.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role createRole(Role role) {

        if (roleRepository.existsById(role.getRoleId())) {
            throw new RuntimeException(
                    "Role ID already exists: " + role.getRoleId()
            );
        }

        return roleRepository.save(role);
    }
}

