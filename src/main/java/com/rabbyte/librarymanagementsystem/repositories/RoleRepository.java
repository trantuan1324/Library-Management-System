package com.rabbyte.librarymanagementsystem.repositories;

import com.rabbyte.librarymanagementsystem.entities.Role;
import com.rabbyte.librarymanagementsystem.utils.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findRoleByRoleName(RoleName roleName);
}
