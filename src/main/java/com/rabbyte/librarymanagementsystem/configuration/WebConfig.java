package com.rabbyte.librarymanagementsystem.configuration;

import com.rabbyte.librarymanagementsystem.entities.Role;
import com.rabbyte.librarymanagementsystem.repositories.RoleRepository;
import com.rabbyte.librarymanagementsystem.utils.enums.RoleName;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class WebConfig {
    private final RoleRepository roleRepository;

    public WebConfig(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Bean
    public CommandLineRunner initRolesData() {
        return args -> {
            validateRoleAndAddNewRole(RoleName.ROLE_ADMIN);
            validateRoleAndAddNewRole(RoleName.ROLE_USER);
            validateRoleAndAddNewRole(RoleName.ROLE_LIBRARIAN);
        };
    }

    private void validateRoleAndAddNewRole(RoleName roleName) {
        this.roleRepository.findRoleByRoleName(roleName)
                .orElseGet(() -> {
                    Role role = new Role(roleName);
                    return this.roleRepository.save(role);
                });
    }
}
